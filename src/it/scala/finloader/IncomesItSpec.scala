package finloader

import org.specs2.mutable.Specification
import ITUtils.db
import scala.slick.session.Database
import org.joda.time.LocalDate
import com.github.tototoshi.csv.DefaultCSVFormat
import finloader.loader.{IncomesLoader, ExpensesLoader}

//import scala.slick.lifted.Query
import finloader.domain.{Income, Incomes, Expenses, Expense}
import scala.slick.driver.PostgresDriver.simple._
import Database.threadLocalSession

/**
 * @author Paul Lysak
 *         Date: 02.07.13
 *         Time: 23:37
 */
class IncomesItSpec extends Specification {

  "IncomesLoader" should {
    "load incomes" in {
      val url1 = getClass.getResource("/inc_2013.csv")
      loader(',').load(url1, "pref_")
      db.withSession {
        val actualIncomes = Query(Incomes).list().toSet
        actualIncomes must be equalTo(sampleIncomes)
      }
   }
  }


  private val sampleIncomes = Set(
    Income(id = "pref_1", date = new LocalDate(2013, 06, 10), amount = 1000000, currency = "UAH", source = "job", comment = "for may 2013"),
    Income(id = "pref_2", date = new LocalDate(2013, 06, 12), amount = 20000, currency = "UAH", source = "sell", comment = "old furniture")
  )

  private def loader(sep: Char) = new IncomesLoader(db)(new DefaultCSVFormat {override val separator = sep})
}
