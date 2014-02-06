package finloader.domain

//import com.github.tototoshi.slick.JodaSupport._
import com.github.tototoshi.slick.GenericJodaSupport
import org.joda.time.LocalDate
//import scala.slick.session.Database
import scala.slick.driver.JdbcDriver
import JdbcDriver._
import scala.slick.driver.JdbcDriver.Implicit._
import scala.slick.lifted.Tag
//import Database.threadLocalSession
//import scala.slick.util.TupleMethods
//import TupleMethods._

/**
 * @author Paul Lysak
 *         Date: 02.07.13
 *         Time: 23:05
 */
case class Expense(id: String, date: LocalDate, amount: Long, currency: String, category: String, comment: String = "")
//case class Expense(id: String, amount: Long, category: String, comment: String = "")

class Expenses(tag: Tag) extends Table[Expense](tag, "expense") {
  object JdbcJodaSupport extends GenericJodaSupport(JdbcDriver)
  import JdbcJodaSupport._

  def id = column[String]("id", O.PrimaryKey)

  def date = column[LocalDate]("date")

  def amount = column[Long]("amount")

  def currency = column[String]("currency")

  def category = column[String]("category")

  def comment = column[String]("comment")

//  def * = id ~ date ~ amount ~ currency ~ category ~ comment <> (Expense.tupled, Expense.unapply _)
//  def * = id ~ amount ~ category ~ comment <> (Expense.tupled, Expense.unapply _)
  def * = (id, date, amount, currency, category, comment) <> (Expense.tupled, Expense.unapply _)

}
