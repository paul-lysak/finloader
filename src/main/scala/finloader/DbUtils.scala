package finloader

import scala.slick.session.Database
import scala.slick.jdbc.meta.MTable
import scala.slick.driver.PostgresDriver.simple._
import Database.threadLocalSession

/**
 * @author Paul Lysak
 *         Date: 13.09.13
 *         Time: 22:56
 */
trait DbUtils {

  def ensureTableCreated(tableObject: Table[_])(implicit db: Database) {
    db.withSession {
      if(MTable.getTables(tableObject.tableName).elements().isEmpty)
        tableObject.ddl.create
    }
  }
}
