package finloader.domain

import org.joda.time.LocalDate
import com.github.tototoshi.slick.GenericJodaSupport
import scala.slick.driver.JdbcDriver
import JdbcDriver._
import scala.slick.driver.JdbcDriver.Implicit._
import scala.slick.lifted.Tag

/**
 * @author Paul Lysak
 *         Date: 15.08.13
 *         Time: 21:47
 */

case class Balance(id: String, snapshotId: String, date: LocalDate, place: String, amount: Long, currency: String, comment: String = "")

class Balances(tag: Tag) extends Table[Balance](tag, "balance") {
  object JdbcJodaSupport extends GenericJodaSupport(JdbcDriver)
  import JdbcJodaSupport._

  def id = column[String]("id", O.PrimaryKey)

  def snapshotId = column[String]("snapshotId")

  def date = column[LocalDate]("date")

  def place = column[String]("place")

  def amount = column[Long]("amount")

  def currency = column[String]("currency")

  def comment = column[String]("comment")

  def * = (id, snapshotId, date, place, amount, currency, comment) <> (Balance.tupled, Balance.unapply _)
}