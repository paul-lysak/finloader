package finloader

import org.rogach.scallop.ScallopConf

/**
 * @author Paul Lysak
 *         Date: 01.08.13
 *         Time: 21:32
 */
object Main {
  def main(args: Array[String]) {
    val conf = new CliConf(args)
    println("hi all")
  }
}

class CliConf(args: Seq[String]) extends ScallopConf(args) {
  val data = opt[String](required=true, descr = "Data directory")
  val config = opt[String](required=true, descr = "Config file")
}