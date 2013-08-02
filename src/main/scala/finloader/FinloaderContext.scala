package finloader

import scala.slick.session.Database
import com.typesafe.config.ConfigFactory
import java.io.File

/**
 * @author Paul Lysak
 *         Date: 01.08.13
 *         Time: 23:24
 */
class FinloaderContext(configFile: File) {
  val config = ConfigFactory.parseFile(configFile)
  val db = Database.forURL(config.getString("database.url"), driver = config.getString("database.driver"))

  val locator = new SourceLocator
  val expensesLoader = new ExpensesLoader(db)
  val finloaderService = new FinloaderService(locator, expensesLoader)
}
