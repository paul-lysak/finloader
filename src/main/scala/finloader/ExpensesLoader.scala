package finloader

import scala.slick.session.Database
import java.io.{File, InputStream, Reader}
import java.net.URL
import com.github.tototoshi.csv.{CSVFormat, CSVReader}
import finloader.domain.{Expenses, Expense}
import java.sql.Date
import scala.slick.driver.PostgresDriver.simple._
import Database.threadLocalSession
import org.joda.time.LocalDate
import org.joda.time.format.ISODateTimeFormat
import scala.slick.jdbc.meta.MTable
import org.slf4j.LoggerFactory


/**
 * @author Paul Lysak
 *         Date: 05.07.13
 *         Time: 21:55
 */
class ExpensesLoader(db: Database)(implicit csvFormat: CSVFormat) {
  def load(source: URL, idPrefix: String = "") {
    log.info(s"Loading expenses from $source")
    log.debug(s"Using CSV separator ${csvFormat.separator}")
    val reader = CSVReader.open(new File(source.toURI))
    var count = 0
    reader.toStream() match {
      case firstRow #:: body =>
        val p = firstRow.zipWithIndex.toMap
        for(row <- body) {
          val r = row.toIndexedSeq
          val expense = Expense(id = idPrefix+r(p("id")),
            date = ISODateTimeFormat.date().parseLocalDate(r(p("date"))),
            amount = (r(p("amount")).toDouble * 100).toLong,
            category = r(p("category")),
            comment = r(p("comment")))
          upsert(expense)
          count += 1
        }
      case _ =>
        println("can't find first line")
    }
    log.info(s"Loaded $count expenses from $source")
  }

  def ensureTablesCreated() {
    db.withSession {
      if(MTable.getTables(Expenses.tableName).elements().isEmpty)
        Expenses.ddl.create
    }
  }

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
