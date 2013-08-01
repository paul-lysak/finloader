package finloader

import scala.slick.session.Database

/**
 * @author Paul Lysak
 *         Date: 01.08.13
 *         Time: 23:24
 */
class FinloaderContext {
  val locator = new SourceLocator
  val db: Database = null //TODO
  val expensesLoader = new ExpensesLoader(db)
  val finloaderService = new FinloaderService(locator, expensesLoader)
}
