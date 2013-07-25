package finloader

import org.specs2.mutable.Specification
import ITUtils.db
import scala.slick.session.Database
import java.sql.Date
import org.joda.time.LocalDate

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
      val url1 = getClass.getResource("/exp_201306.csv")
      loader.load(url1, "pref_")
      db.withSession {
        val actualExpenses = Query(Expenses).list().toSet
        actualExpenses must be equalTo(sampleExpenses)
      }

      val url2 = getClass.getResource("/exp_201306_.csv")
      loader.load(url2, "pref_")
      db.withSession {
        val actualExpenses = Query(Expenses).list().toSet
        actualExpenses must be equalTo(mergedExpenses)
      }
    }
  }


  private val sampleExpenses = Set(
    Expense(id = "pref_1", date = new LocalDate(2013, 06, 10), amount = 10000, category = "food", comment = "supermarket"),
    Expense(id = "pref_2", date = new LocalDate(2013, 06, 11), amount = 35050, category = "household"),
    Expense(id = "pref_3", date = new LocalDate(2013, 06, 12), amount = 32000, category = "car_fuel", comment = "30L")
  )

  private val mergedExpenses = Set(
    Expense(id = "pref_1", date = new LocalDate(2013, 06, 10), amount = 10000, category = "food", comment = "supermarket"),
    Expense(id = "pref_2", date = new LocalDate(2013, 06, 15), amount = 55050, category = "household", comment = "repair something"),
    Expense(id = "pref_3", date = new LocalDate(2013, 06, 12), amount = 32000, category = "car_fuel", comment = "30L"),
    Expense(id = "pref_4", date = new LocalDate(2013, 06, 13), amount = 22000, category = "food", comment = "fruits")
  )

  private lazy val loader = new ExpensesLoader(db)
}
