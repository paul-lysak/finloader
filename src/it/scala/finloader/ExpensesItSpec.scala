package finloader

import org.specs2.mutable.Specification
import ITUtils.db
import scala.slick.session.Database
import java.sql.Date
import org.joda.time.LocalDate
import com.github.tototoshi.csv.DefaultCSVFormat
import finloader.loader.ExpensesLoader

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
  sequential

  "ExpensesLoader" should {
    "load expenses" in {
      cleanExpenses

      loadFile("/exp_201306.csv", ',', sampleExpenses)
   }

    "merge expenses" in {
      loadFile("/exp_201306_.csv", ';', mergedExpenses)
    }

    "substitute skipped date" in {
      cleanExpenses
      loadFile("/exp_201306_sk.csv", ',', skippedDateExpenses)
    }
  }

  private def cleanExpenses {
      db.withSession {
          Query(Expenses).delete
          val exp = Query(Expenses).list().toSet
          exp must be equalTo(Set())
      }
  }

  private def loadFile(path: String, separator: Char, expectedContent: Set[Expense]) {
      val url1 = getClass.getResource(path)
      loader(separator).load(url1, "pref_")
      db.withSession {
        val actualExpenses = Query(Expenses).list().toSet
        actualExpenses must be equalTo(expectedContent)
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

  private val skippedDateExpenses = Set(
    Expense(id = "pref_1", date = new LocalDate(2013, 06, 10), amount = 10000, category = "food", comment = "supermarket"),
    Expense(id = "pref_2", date = new LocalDate(2013, 06, 10), amount = 35050, category = "household"),
    Expense(id = "pref_3", date = new LocalDate(2013, 06, 10), amount = 7000, category = "transport", comment = "taxi"),
    Expense(id = "pref_4", date = new LocalDate(2013, 06, 12), amount = 32000, category = "car_fuel", comment = "30L")
  )

  private def loader(sep: Char) = new ExpensesLoader(db)(new DefaultCSVFormat {override val separator = sep})
}
