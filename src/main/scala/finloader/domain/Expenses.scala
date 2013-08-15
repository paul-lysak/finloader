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
case class Expense(id: String, date: LocalDate, amount: Long, category: String, comment: String = "")

object Expenses extends Table[Expense]("expense") {
  def id = column[String]("id", O.PrimaryKey)

  def date = column[LocalDate]("date")

  def amount = column[Long]("amount")

  def category = column[String]("category")

  def comment = column[String]("comment")

  def * = id ~ date ~ amount ~ category ~ comment <> (Expense, Expense.unapply _)
}
