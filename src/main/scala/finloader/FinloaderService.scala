package finloader

import java.net.URL
import java.io.File

/**
 * @author Paul Lysak
 *         Date: 01.08.13
 *         Time: 23:20
 */
class FinloaderService(locator: SourceLocator, expensesLoader: ExpensesLoader) {
  expensesLoader.ensureTablesCreated()

  def loadData(folderUrl: URL) {
    locator.locateExpenses(folderUrl).foreach(
      fileUrl => {
        expensesLoader.load(fileUrl, idPrefix(fileUrl))
      }
    )
  }

  private def idPrefix(url: URL) = {
      val file = new File(url.toURI)
      file.getName.toLowerCase.stripSuffix(".csv") + "_"
  }
}
