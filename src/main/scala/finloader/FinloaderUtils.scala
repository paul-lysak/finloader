package finloader

/**
 * @author Paul Lysak
 *         Date: 15.08.13
 *         Time: 22:44
 */
object FinloaderUtils {
  def parseAmount(amt: String): (Long, String) = {
    val parts = amt.split(" ", 2).toList
    val amount = (parts.head.toDouble * 100).toLong
    val currency = parts.tail.headOption.getOrElse("UAH")
    (amount, currency)
  }

}
