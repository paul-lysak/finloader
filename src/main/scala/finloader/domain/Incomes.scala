package finloader.domain

import com.github.tototoshi.slick.JodaSupport._
import org.joda.time.LocalDate

//import scala.slick.session.Database
import scala.slick.driver.JdbcDriver._
import scala.slick.driver.JdbcDriver.Implicit._
import scala.slick.lifted.Tag
//import scala.slick.driver.PostgresDriver.simple._
//import Database.threadLocalSession
import scala.slick.util.TupleMethods._

/**
 * @author Paul Lysak
 *         Date: 02.07.13
 *         Time: 23:05
 */
//case class Income(id: String, date: LocalDate, amount: Long, currency: String, source: String, comment: String = "")
case class Income(id: String, amount: Long, currency: String, source: String, comment: String = "")

class Incomes(tag: Tag) extends Table[Income](tag, "income") {
  def id = column[String]("id", O.PrimaryKey)

//  def date = column[LocalDate]("date")

  def amount = column[Long]("amount")

  def currency = column[String]("currency")

  def source = column[String]("source")

  def comment = column[String]("comment")

//  def * = id ~ date ~ amount ~ currency ~ source ~ comment <> (Income.tupled, Income.unapply _)
  def * = id ~ amount ~ currency ~ source ~ comment <> (Income.tupled, Income.unapply _)
}
