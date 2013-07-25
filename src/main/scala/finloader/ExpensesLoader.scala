package finloader

import scala.slick.session.Database
import java.io.{File, InputStream, Reader}
import java.net.URL
import com.github.tototoshi.csv.CSVReader
import finloader.domain.{Expenses, Expense}
import java.sql.Date
import scala.slick.driver.PostgresDriver.simple._
import Database.threadLocalSession
import org.joda.time.LocalDate
import org.joda.time.format.ISODateTimeFormat


/**
 * @author Paul Lysak
 *         Date: 05.07.13
 *         Time: 21:55
 */
class ExpensesLoader(db: Database) {
  def load(source: URL, idPrefix: String = "") {
    val reader = CSVReader.open(new File(source.toURI))
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
//          println("row: "+expense)
          upsert(expense)
        }
      case _ =>
        println("can't find first line")
    }
  }

  private def upsert(expense: Expense) {
    db.withSession {
      Query(Expenses).map(_.id).filter(_ === expense.id).firstOption() match {
        case Some(existingId) =>
          Expenses.where(_.id === existingId).update(expense)
        case None => {
          Expenses.insert(expense)
        }
      }
    }
  }
}
