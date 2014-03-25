package finloader.domain

import com.github.tototoshi.slick.JdbcJodaSupport._
import org.joda.time.LocalDate
import scala.slick.driver.JdbcDriver
import JdbcDriver._
import scala.slick.driver.JdbcDriver.Implicit._
import scala.slick.lifted.{TableQuery, Tag}

/**
 * @author Paul Lysak
 *         Date: 02.07.13
 *         Time: 23:05
 */
case class Expense(id: String, date: LocalDate, amount: Long, currency: String, category: String, comment: String = "")

class Expenses(tag: Tag) extends Table[Expense](tag, "expense") {
  def id = column[String]("id", O.PrimaryKey, O.DBType("VARCHAR(64)"))

  def date = column[LocalDate]("date")

  def amount = column[Long]("amount")

  def currency = column[String]("currency", O.DBType("VARCHAR(8)"))

  def category = column[String]("category", O.DBType("VARCHAR(64)"))

  def comment = column[String]("comment", O.DBType("TEXT"))


  def * = (id, date, amount, currency, category, comment) <> (Expense.tupled, Expense.unapply _)


  def dateIndex = index("expense_date_index", date)
}

case class ExpenseTag(id: Option[Long], expenseId: String, tag: String)

class ExpenseTags(t: Tag) extends Table[ExpenseTag](t, "expense_tags") {
  def id = column[Long]("id", O.PrimaryKey, O.DBType("SERIAL"))//O.AutoInc doesnt' work directly

  def expenseId = column[String]("expense_id", O.DBType("VARCHAR(64)"))

  def tag = column[String]("tag", O.DBType("VARCHAR(64)"))


//  def autoInc = (expenseId, tag) <> (ExpenseTag.tupled, ExpenseTag.unapply _) returning id

  def * = (id.?, expenseId, tag) <> (ExpenseTag.tupled, ExpenseTag.unapply _)

//  def withoutId = (expenseId, tag) returning id


  def uniquePairIndex = index("expense_tag_pair", (expenseId, tag), unique = true)

  def expense = foreignKey("expense_fk", expenseId, TableQuery[Expenses])(_.id)
}

