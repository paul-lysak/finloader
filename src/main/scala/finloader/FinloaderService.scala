package finloader

import java.net.URL

/**
 * @author Paul Lysak
 *         Date: 01.08.13
 *         Time: 23:20
 */
class FinloaderService(locator: SourceLocator, expensesLoader: ExpensesLoader) {

  def loadData(folderUrl: URL) {
    println(s"TODO: load from $folderUrl")
    locator.locateExpenses(folderUrl).foreach(println)
  }
}
