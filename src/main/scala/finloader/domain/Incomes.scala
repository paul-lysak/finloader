package finloader.domain

import com.github.tototoshi.slick.JodaSupport._
import org.joda.time.LocalDate

//import scala.slick.session.Database
import scala.slick.driver.PostgresDriver.simple._
//import Database.threadLocalSession

/**
 * @author Paul Lysak
 *         Date: 02.07.13
 *         Time: 23:05
 */
case class Income(id: String, date: LocalDate, amount: Long, currency: String, source: String, comment: String = "")

object Incomes extends Table[Income]("income") {
  def id = column[String]("id", O.PrimaryKey)

  def date = column[LocalDate]("date")

  def amount = column[Long]("amount")

  def currency = column[String]("currency")

  def source = column[String]("source")

  def comment = column[String]("comment")

  def * = id ~ date ~ amount ~ currency ~ source ~ comment <> (Income, Income.unapply _)
}
