package finloader

import org.specs2.mutable.Specification
import ITUtils.db
import scala.slick.session.Database
import java.sql.Date

//import scala.slick.lifted.Query
import finloader.domain.{Expenses, Expense}
import scala.slick.driver.PostgresDriver.simple._
import Database.threadLocalSession

/**
 * @author Paul Lysak
 *         Date: 02.07.13
 *         Time: 23:37
 */
class ExpensesItSpec extends Specification {

  "ExpensesLoader" should {
    "load expenses" in {
      val url = getClass.getResource("/exp_201306.csv")
      loader.load(url)
      db.withSession {
//        Expenses.insert(Expense(id = "1", date = new Date(2013, 06, 01), amount = 1234, category = "cat1", comment = "com"))
        val actualExpenses = Query(Expenses).list().toSet
//        println("Actual expenses: "+actualExpenses)
        actualExpenses must be equalTo(sampleExpenses)
      }
    }
  }


  private val sampleExpenses = Set(
  //TODO actual data from exp*.csv
    Expense(id = "201306_1", date = new Date(2013, 06, 10), amount = 100, category = "food", comment = "supermarket"),
    Expense(id = "201306_2", date = new Date(2013, 06, 11), amount = 350, category = "household"),
    Expense(id = "201306_3", date = new Date(2013, 06, 12), amount = 320, category = "car_fuel", comment = "30L")

  )

  private lazy val loader = new ExpensesLoader(db)
}
