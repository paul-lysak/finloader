package finloader

import scala.slick.session.Database
import com.typesafe.config.ConfigFactory
import java.io.File
import scala.collection.JavaConversions._
import com.github.tototoshi.csv.DefaultCSVFormat
import org.slf4j.LoggerFactory
import finloader.loader.{IncomesLoader, BalancesLoader, ExpensesLoader}

/**
 * @author Paul Lysak
 *         Date: 01.08.13
 *         Time: 23:24
 */
class FinloaderContext(configFile: File) {
  private val fallbackConfig = ConfigFactory.parseMap(Map("csv.separator" -> ","))
  val config = ConfigFactory.parseFile(configFile).withFallback(fallbackConfig)
  val db = Database.forURL(config.getString("database.url"), driver = config.getString("database.driver"))

  implicit private val csvFormat = new DefaultCSVFormat {
    override val separator = config.getString("csv.separator").head
  }
  val locator = new SourceLocator
  val expensesLoader = new ExpensesLoader(db)
  val balancesLoader = new BalancesLoader(db)
  val incomesLoader = new IncomesLoader(db)

  val finloaderService = new FinloaderService(locator, expensesLoader, balancesLoader, incomesLoader)

  private val log = LoggerFactory.getLogger(classOf[FinloaderContext])
}
