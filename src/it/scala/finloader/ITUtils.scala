package finloader

import com.typesafe.config.ConfigFactory
import java.io.File
import scala.slick.session.Database
import finloader.domain.Expenses
import scala.slick.driver.PostgresDriver.simple._
import Database.threadLocalSession

/**
 * @author Paul Lysak
 *         Date: 04.07.13
 *         Time: 23:20
 */
object ITUtils {
  lazy val config = ConfigFactory.parseFile(new File("it.conf"))

  lazy val db = {
    val d = Database.forURL(config.getString("database.testUrl"), driver = config.getString("database.driver"))
    createSchema(d)
    d
  }

  private def createSchema(d: Database) {
    println("creating DB schema...")
    d withSession  {
      Expenses.ddl.create
    }
  }
}
