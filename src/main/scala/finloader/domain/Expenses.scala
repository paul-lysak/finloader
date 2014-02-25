package finloader.domain

import com.github.tototoshi.slick.JdbcJodaSupport._
import org.joda.time.LocalDate
import scala.slick.driver.JdbcDriver
import JdbcDriver._
import scala.slick.driver.JdbcDriver.Implicit._
import scala.slick.lifted.Tag

/**
 * @author Paul Lysak
 *         Date: 02.07.13
 *         Time: 23:05
 */
case class Expense(id: String, date: LocalDate, amount: Long, currency: String, category: String, comment: String = "")

class Expenses(tag: Tag) extends Table[Expense](tag, "expense") {
  def id = column[String]("id", O.PrimaryKey)

  def date = column[LocalDate]("date")

  def amount = column[Long]("amount")

  def currency = column[String]("currency")

  def category = column[String]("category")

  def comment = column[String]("comment")

  def * = (id, date, amount, currency, category, comment) <> (Expense.tupled, Expense.unapply _)
}
