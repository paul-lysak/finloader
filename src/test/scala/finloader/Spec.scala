package finloader

import org.specs2.mutable.Specification
import scala.slick.session.Database
import scala.slick.driver.PostgresDriver.simple._
import Database.threadLocalSession

/**
 * @author Paul Lysak
 *         Date: 27.06.13
 *         Time: 23:13
 */
class Spec extends Specification {


  "it" should {
    println("hello world")

    Database.forURL("jdbc:postgresql://localhost/finloader?user=dbadmin&password=dbadmin", driver = "org.postgresql.Driver") withSession {
      println("hello session")

      Query(Suppliers) foreach {
        case (key, value) =>
          println("kv = "+key+", "+value)
      }
    }
  }
}

object Suppliers extends Table[(Int, String)]("tab") {
  def id = column[Int]("key", O.PrimaryKey) // This is the primary key column
  def value = column[String]("value")
  // Every table needs a * projection with the same type as the table's type parameter
  def * = id ~ value
}