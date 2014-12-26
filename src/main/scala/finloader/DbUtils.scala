package finloader

import scala.slick.jdbc.JdbcBackend.Database
import scala.slick.driver.JdbcDriver.simple._
import scala.slick.jdbc.meta.MTable
import scala.slick.lifted.{TableQuery}
import scala.slick.ast.TableNode
import Database.dynamicSession

/**
 * @author Paul Lysak
 *         Date: 13.09.13
 *         Time: 22:56
 */
trait DbUtils {
  def ensureTableCreated[E <: Table[_]](tableQueryObject: TableQuery[E])(implicit db: Database) {
    //TODO get rid of dynamic session
    db.withDynSession {
        if(MTable.getTables(tableQueryObject.unpackable.value.tableName).firstOption().isEmpty)
          tableQueryObject.ddl.create
    }
  }
}
