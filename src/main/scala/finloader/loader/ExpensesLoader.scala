package finloader.loader

import scala.slick.jdbc.JdbcBackend.Database
import scala.slick.driver.JdbcDriver.simple._
import java.io.{File}
import java.net.URL
import com.github.tototoshi.csv.{CSVFormat, CSVReader}
import finloader.domain.{ExpenseTag, ExpenseTags, Expenses, Expense}
import scala.slick.lifted.TableQuery
import org.slf4j.LoggerFactory
import finloader.{FinloaderUtils, DbUtils}
import finloader.FinloaderUtils._


/**
 * @author Paul Lysak
 *         Date: 05.07.13
 *         Time: 21:55
 */
class ExpensesLoader(db: Database)(implicit csvFormat: CSVFormat) extends DataLoader with DbUtils {
  private implicit val dbImpl = db

  def load(source: URL, idPrefix: String = "") {
    log.info(s"Loading expenses from $source")
    log.debug(s"Using CSV separator ${csvFormat.separator}")
    val reader = CSVReader.open(new File(source.toURI))
    var count = 0
    val expensesStream: Stream[(Expense, String)] =  (reader.toStream() match {
      case firstRow #:: body =>
        val p = firstRow.zipWithIndex.toMap

        for(row <- body) yield {
          val r = row.toIndexedSeq
          val (amt, curr) = FinloaderUtils.parseAmount(r(p("amount")))
          count += 1
          (Expense(id = idPrefix+r(p("id")),
            date = parseDate(r(p("date"))),
            amount = amt,
            currency = curr,
            category = r(p("category")),
            comment = r(p("comment"))),
          r(p("tags")))
        }
      case _ =>
        log.error("can't find first line")
        Stream()
    })

    lazy val defaultedExpenses: Stream[(Expense, String)] = ((Expense(null, null, 0, null, null), "") #:: defaultedExpenses).zip(expensesStream).
          map({case ((prevExp, prevTags), (thisExp, thisTags)) =>
        val date = if(thisExp.date == null) prevExp.date else thisExp.date
      (thisExp.copy(date = date), thisTags)
    })

   defaultedExpenses.foreach(upsert.tupled)

    log.info(s"Loaded $count expenses from $source")
  }

  def ensureTablesCreated() = {
    ensureTableCreated(TableQuery[Expenses])
    ensureTableCreated(TableQuery[ExpenseTags])
  }


  private val upsert = {(expense: Expense, tagsString: String) =>
    db.withSession {
      implicit session =>
      val expQuery = TableQuery[Expenses]
      expQuery.map(_.id).filter(_ === expense.id).firstOption() match {
        case Some(existingId) => {
          log.debug(s"Update $existingId")
          expQuery.where(_.id === existingId).update(expense)
        }
        case None => {
          expQuery.insert(expense)
        }
      }
      updateTags(expense.id, expense.category, tagsString)
    }
  }//end upsert

  private def updateTags(expenseId: String, category: String, tagsString: String)(implicit session: Session) {
        val expenseTags = TableQuery[ExpenseTags]
        expenseTags.where(_.expenseId === expenseId).delete
        val tags = tagsString.split(" ").filter(_.nonEmpty) :+ category
        expenseTags.map(et => (et.expenseId, et.tag)) ++= tags.map(t => (expenseId, t))
  }

  val log = LoggerFactory.getLogger(classOf[ExpensesLoader])
}
