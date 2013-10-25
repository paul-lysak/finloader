package finloader.domain

import org.joda.time.LocalDate
//import com.github.tototoshi.slick.JodaSupport._
import scala.slick.driver.JdbcDriver._
import scala.slick.driver.JdbcDriver.Implicit._
import scala.slick.lifted.Tag
import scala.slick.util.TupleMethods
import TupleMethods._

/**
 * @author Paul Lysak
 *         Date: 15.08.13
 *         Time: 21:47
 */

//case class Balance(id: String, snapshotId: String, date: LocalDate, place: String, amount: Long, currency: String, comment: String = "")
case class Balance(id: String, snapshotId: String, place: String, amount: Long, currency: String, comment: String = "") {
  val date: LocalDate = ???
}

class Balances(tag: Tag) extends Table[Balance](tag, "balance") {

  def id = column[String]("id", O.PrimaryKey)

  def snapshotId = column[String]("snapshotId")

  //TODO
//  def date = column[LocalDate]("date")

  def place = column[String]("place")

  def amount = column[Long]("amount")

  def currency = column[String]("currency")

  def comment = column[String]("comment")

//  def * = (id, snapshotId, place, amount, currency, comment) <> (Balance.tupled, Balance.unapply _)
  def * = id ~ snapshotId ~ place ~ amount ~ currency ~ comment <> (Balance.tupled, Balance.unapply _)
//  def * = id ~ snapshotId ~ date ~ place ~ amount ~ currency ~ comment <> (Balance.tupled, Balance.unapply _)
}