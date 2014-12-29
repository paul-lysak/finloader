package finloader

import org.specs2.mutable.Specification
import java.io.File

/**
 * @author Paul Lysak
 *         Date: 25.07.13
 *         Time: 23:34
 */
class SourceLocatorSpec extends Specification {

  private val sl = new SourceLocator("exp_")

  "SourceLocator" should {
    "find expenses" in {
      val urls = sl.locate(getClass.getResource("/sample_ds"))
      val names = urls.map(url => (new File(url.getFile)).getName).toSet
      names must be equalTo(Set("exp_201306.csv"))
    }
  }
}
