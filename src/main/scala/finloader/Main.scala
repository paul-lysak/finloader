package finloader

import org.rogach.scallop.ScallopConf
import java.io.File

/**
 * @author Paul Lysak
 *         Date: 01.08.13
 *         Time: 21:32
 */
object Main {
  def main(args: Array[String]) {
    val conf = new CliConf(args)
    val ctx = new FinloaderContext
    val dataFolder = new File(conf.data()).toURI.toURL
    ctx.finloaderService.loadData(dataFolder)
    println("hi all")
  }
}

class CliConf(args: Seq[String]) extends ScallopConf(args) {
  val data = opt[String](required=true, descr = "Data directory")
  val config = opt[String](required=true, descr = "Config file")
}

