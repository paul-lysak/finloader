package finloader.domain

import org.joda.time.LocalDate
import com.github.tototoshi.slick.JodaSupport._
import scala.slick.driver.PostgresDriver.simple._

/**
 * @author Paul Lysak
 *         Date: 15.08.13
 *         Time: 21:47
 */

case class Balance(id: Long, snapshotId: String, date: LocalDate, place: String, amount: Long, currency: String, comment: String = "")

object Balances extends Table[Balance]("balance") {
  def id = column[Long]("id", O.PrimaryKey)

  def snapshotId = column[String]("snapshotId")

  def date = column[LocalDate]("date")

  def place = column[String]("place")

  def amount = column[Long]("amount")

  def currency = column[String]("currency")

  def comment = column[String]("comment")

  def * = id ~ snapshotId ~ date ~ place ~ amount ~ currency ~ comment <> (Balance, Balance.unapply _)
}