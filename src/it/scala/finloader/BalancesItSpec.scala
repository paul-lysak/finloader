package finloader

import org.specs2.mutable.Specification
import ITUtils.db
import scala.slick.session.Database
import org.joda.time.LocalDate
import com.github.tototoshi.csv.DefaultCSVFormat

//import scala.slick.lifted.Query
import finloader.domain.{Balance, Balances, Expenses, Expense}
import scala.slick.driver.PostgresDriver.simple._
import Database.threadLocalSession

/**
 * @author Paul Lysak
 *         Date: 02.07.13
 *         Time: 23:37
 */
class BalancesItSpec extends Specification {

  "BalancesLoader" should {
    "load balances" in {
      val url1 = getClass.getResource("/chk_201306.csv")
      loader(',').load(url1, "pref_")
      db.withSession {
        val actualBalances = Query(Balances).list().toSet
        actualBalances must be equalTo(sampleBalances)
      }
   }
  }


  private val sampleBalances = Set(
    Balance(id = 1, snapshotId = "pref_1", date = new LocalDate(2013, 05, 10), place = "cash", amount = 200, currency = "USD", comment = ""),
    Balance(id = 2, snapshotId = "pref_2", date = new LocalDate(2013, 05, 10), place = "cash", amount = 500, currency = "UAH", comment = "")
  )

  private def loader(sep: Char) = new BalancesLoader(db)(new DefaultCSVFormat {override val separator = sep})
}
