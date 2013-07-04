import com.typesafe.config.ConfigFactory
import java.io.File
import scala.slick.jdbc.StaticQuery
import scala.slick.session.Database
import scala.slick.driver.PostgresDriver.simple._
import Database.threadLocalSession


object DB {
  val db = Database.forURL(config.getString("database.setupUrl"), driver = config.getString("database.driver"))

  def create {
     drop
     db.withSession {
       println(s"Creating DB $dbName...")
       StaticQuery.updateNA(s"CREATE DATABASE $dbName").execute()
     }
  }

  def drop {
    db.withSession {
      println(s"Dropping DB $dbName...")
      StaticQuery.updateNA(s"DROP DATABASE IF EXISTS $dbName").execute()
    }

  }

  lazy val dbName = config.getString("database.name")

  lazy val config = ConfigFactory.parseFile(new File("it.conf"))

}