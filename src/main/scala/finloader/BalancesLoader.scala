package finloader

import com.github.tototoshi.csv.CSVFormat
import scala.slick.session.Database
import java.net.URL

/**
 * @author Paul Lysak
 *         Date: 15.08.13
 *         Time: 21:43
 */
class BalancesLoader(db: Database)(implicit csvFormat: CSVFormat) extends DataLoader {
  def load(source: URL, idPrefix: String) {}
}
