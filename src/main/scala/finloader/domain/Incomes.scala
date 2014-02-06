package finloader.domain

import com.github.tototoshi.slick.GenericJodaSupport
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
case class Income(id: String, date: LocalDate, amount: Long, currency: String, source: String, comment: String = "")

class Incomes(tag: Tag) extends Table[Income](tag, "income") {
  object JdbcJodaSupport extends GenericJodaSupport(JdbcDriver)
  import JdbcJodaSupport._

  def id = column[String]("id", O.PrimaryKey)

  def date = column[LocalDate]("date")

  def amount = column[Long]("amount")

  def currency = column[String]("currency")

  def source = column[String]("source")

  def comment = column[String]("comment")

  def * = (id, date, amount, currency, source, comment) <> (Income.tupled, Income.unapply _)
}
