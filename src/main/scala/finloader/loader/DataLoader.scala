package finloader.loader

import java.net.URL

/**
 * @author Paul Lysak
 *         Date: 15.08.13
 *         Time: 21:44
 */
trait DataLoader {
  def load(source: URL, idPrefix: String = "")

  //TODO do we need it? I think no, but need some time to decide finally.
  def ensureTablesCreated()
}
