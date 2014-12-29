package finloader

import java.net.URL
import java.io.File
import org.slf4j.LoggerFactory
import finloader.loader.{ExchangeRatesLoader, IncomesLoader, BalancesLoader, ExpensesLoader}

/**
 * @author Paul Lysak
 *         Date: 01.08.13
 *         Time: 23:20
 */
class FinloaderService(locator1: SourceLocator,
                      locator2: SourceLocator,
                      locator3: SourceLocator,
                      locator4: SourceLocator,
                       expensesLoader: ExpensesLoader,
                       balancesLoader: BalancesLoader,
                       incomesLoader: IncomesLoader,
                       ratesLoader: ExchangeRatesLoader) {
  expensesLoader.ensureTablesCreated()
  balancesLoader.ensureTablesCreated()
  incomesLoader.ensureTablesCreated()
  ratesLoader.ensureTablesCreated();

  def loadData(folderUrl: URL) {
    log.info(s"Loading data from $folderUrl...")
    locator1.locate(folderUrl).foreach( fileUrl =>  expensesLoader.load(fileUrl, idPrefix(fileUrl)) )
    locator2.locate(folderUrl).foreach( fileUrl =>  balancesLoader.load(fileUrl, idPrefix(fileUrl)) )
    locator3.locate(folderUrl).foreach( fileUrl =>  incomesLoader.load(fileUrl, idPrefix(fileUrl)) )
    locator4.locate(folderUrl).foreach( fileUrl =>  ratesLoader.load(fileUrl, idPrefix(fileUrl)) )
    log.info(s"Finished loading data from $folderUrl")
  }

  private def idPrefix(url: URL) = {
      val file = new File(url.toURI)
      file.getName.toLowerCase.stripSuffix(".csv") + "_"
  }

  private val log = LoggerFactory.getLogger(classOf[FinloaderService])
}
