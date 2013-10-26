package finloader

import com.typesafe.config.ConfigFactory
import java.io.File
import scala.slick.lifted.TableQuery
//import scala.slick.session.Database
import scala.slick.jdbc.JdbcBackend.Database
import finloader.domain.{Incomes, Balances, Expenses}
import scala.slick.driver.JdbcDriver.simple._
import Database.dynamicSession

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
    d withDynSession {
      TableQuery[Expenses].ddl.create
      TableQuery[Balances].ddl.create
      TableQuery[Incomes].ddl.create
//      val tq: TableQuery[_, _#TableElementType] = TableQuery[Incomes]
//      tq.ddl.create
    }
  }
}
