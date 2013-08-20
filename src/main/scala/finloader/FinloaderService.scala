package finloader

import java.net.URL
import java.io.File
import org.slf4j.LoggerFactory
import finloader.loader.{IncomesLoader, BalancesLoader, ExpensesLoader}

/**
 * @author Paul Lysak
 *         Date: 01.08.13
 *         Time: 23:20
 */
class FinloaderService(locator: SourceLocator, expensesLoader: ExpensesLoader, balancesLoader: BalancesLoader, incomesLoader: IncomesLoader) {
  expensesLoader.ensureTablesCreated()
  balancesLoader.ensureTablesCreated()
  incomesLoader.ensureTablesCreated()

  def loadData(folderUrl: URL) {
    log.info(s"Loading data from $folderUrl...")
    locator.locateExpenses(folderUrl).foreach( fileUrl =>  expensesLoader.load(fileUrl, idPrefix(fileUrl)) )
    locator.locateBalances(folderUrl).foreach( fileUrl =>  balancesLoader.load(fileUrl, idPrefix(fileUrl)) )
    locator.locateIncomes(folderUrl).foreach( fileUrl =>  incomesLoader.load(fileUrl, idPrefix(fileUrl)) )
    log.info(s"Finished loading data from $folderUrl")
  }

  private def idPrefix(url: URL) = {
      val file = new File(url.toURI)
      file.getName.toLowerCase.stripSuffix(".csv") + "_"
  }

  private val log = LoggerFactory.getLogger(classOf[FinloaderService])
}
