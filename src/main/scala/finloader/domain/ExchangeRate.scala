package finloader.domain

import org.joda.time.LocalDate
import scala.slick.lifted.Tag
import com.github.tototoshi.slick.GenericJodaSupport
import scala.slick.driver.JdbcDriver
import JdbcDriver._
import scala.slick.driver.JdbcDriver.Implicit._
/**
 * @author Paul Lysak
 *         Date: 07.02.14
 *         Time: 22:17
 */
case class ExchangeRate(id: String, date: LocalDate, currency: String, rate: BigDecimal, comment: String = "")

class ExchangeRates (tag: Tag) extends Table[ExchangeRate](tag, "rate") {
  object JdbcJodaSupport extends GenericJodaSupport(JdbcDriver)
  import JdbcJodaSupport._

  def id = column[String]("id", O.PrimaryKey)

  def date = column[LocalDate]("date")

  def currency = column[String]("currency")

  def rate = column[BigDecimal]("rate")

  def comment = column[String]("comment")

  def * = (id, date, currency, rate, comment) <> (ExchangeRate.tupled, ExchangeRate.unapply _)
}
