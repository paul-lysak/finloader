package finloader

import java.net.URL
import java.io.{FilenameFilter, File}
import scala.collection.JavaConversions._

/**
 * @author Paul Lysak
 *         Date: 25.07.13
 *         Time: 23:31
 */
class SourceLocator {

  def locateExpenses(baseUrl: URL): Seq[URL]  = {
    val file = new File(baseUrl.toURI)
    val files = file.listFiles(ExpensesFilter)
    files.map(_.toURI.toURL)
  }

  private object ExpensesFilter extends Filter("exp_")

  private class Filter(prefix: String) extends FilenameFilter {
    private def sub(dir: File, name: String): File =
      new File(dir.getAbsolutePath + "/" + name)

    def accept(dir: File, name: String) = {
       name.endsWith(".csv") && name.startsWith(prefix) && sub(dir, name).isFile
    }
  }
}
