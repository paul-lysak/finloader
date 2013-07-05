package finloader

import scala.slick.session.Database
import java.io.{InputStream, Reader}
import java.net.URL

/**
 * @author Paul Lysak
 *         Date: 05.07.13
 *         Time: 21:55
 */
class ExpensesLoader(db: Database) {
  def load(source: URL) {
    println(s"TODO load from $source")
    //TODO
  }

}
