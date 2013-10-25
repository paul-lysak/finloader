package finloader.loader

import scala.slick.jdbc.JdbcBackend.Database
import java.io.{File}
import java.net.URL
import com.github.tototoshi.csv.{CSVFormat, CSVReader}
import finloader.domain.{Expenses, Expense}
//import scala.slick.driver.PostgresDriver.simple._
import Database.dynamicSession
import org.slf4j.LoggerFactory
import finloader.DbUtils
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
    val expensesStream: Stream[Expense] =  (reader.toStream() match {
      case firstRow #:: body =>
        val p = firstRow.zipWithIndex.toMap
        for(row <- body) yield {
          val r = row.toIndexedSeq
          count += 1
          Expense(id = idPrefix+r(p("id")),
            date = parseDate(r(p("date"))),
            amount = (r(p("amount")).toDouble * 100).toLong,
            category = r(p("category")),
            comment = r(p("comment")))
        }
      case _ =>
        log.error("can't find first line")
        Stream()
    })

    lazy val defaultedExpenses: Stream[Expense] = (Expense(null, null, 0, null) #:: defaultedExpenses).zip(expensesStream).
          map({case (prevExp, thisExp) =>
        val date = if(thisExp.date == null) prevExp.date else thisExp.date
        thisExp.copy(date = date)})

   defaultedExpenses.foreach(upsert)

    log.info(s"Loaded $count expenses from $source")
  }

  def ensureTablesCreated() = ??? //ensureTableCreated(Expenses)


  private def upsert(expense: Expense) {
    db.withSession {
      Query(Expenses).map(_.id).filter(_ === expense.id).firstOption() match {
        case Some(existingId) => {
          log.debug(s"Update $existingId")
          Expenses.where(_.id === existingId).update(expense)
        }
        case None => {
          Expenses.insert(expense)
        }
      }
    }
  }//end upsert

  val log = LoggerFactory.getLogger(classOf[ExpensesLoader])
}
