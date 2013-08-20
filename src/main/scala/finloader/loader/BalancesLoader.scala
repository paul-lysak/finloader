package finloader.loader

import com.github.tototoshi.csv.{CSVReader, CSVFormat}
import scala.slick.session.Database
import java.net.URL
import org.slf4j.LoggerFactory
import scala.slick.jdbc.meta.MTable
import finloader.domain.{Balance, Expense, Balances}
import scala.slick.session.Database
import scala.slick.driver.PostgresDriver.simple._
import Database.threadLocalSession
import org.joda.time.format.ISODateTimeFormat
import java.io.File
import finloader.FinloaderUtils

/**
 * @author Paul Lysak
 *         Date: 15.08.13
 *         Time: 21:43
 */
class BalancesLoader(db: Database)(implicit csvFormat: CSVFormat) extends DataLoader {
  def load(source: URL, idPrefix: String) {
    log.info(s"Loading balances from $source")
    log.debug(s"Using CSV separator ${csvFormat.separator}")
    val reader = CSVReader.open(new File(source.toURI))
    var snapshot = ("", Seq[Balance]())
    var count = 0
    reader.toStream() match {
      case firstRow #:: body =>
        val p = firstRow.zipWithIndex.toMap
        for(row <- body) {
          val r = row.toIndexedSeq
          val (amt, curr) = FinloaderUtils.parseAmount(r(p("amount")))
          val balance = Balance(id = idPrefix+(count + 1),
            snapshotId = idPrefix+r(p("snapshotId")),
            date = ISODateTimeFormat.date().parseLocalDate(r(p("date"))),
            place = r(p("place")),
            amount = amt,
            currency = curr,
            comment = r(p("comment")))
          snapshot = addItem(balance, snapshot._1, snapshot._2)
          count += 1
        }
      case _ =>
        log.error("can't find first line")
    }
    upsertSnapshot(snapshot._1, snapshot._2)
    log.info(s"Loaded $count balances from $source")
  }

  private def addItem(item: Balance, snapshotId: String, snapshotItems: Seq[Balance]): (String, Seq[Balance]) = {
    if(item.snapshotId == snapshotId || snapshotItems.isEmpty) {
      (item.snapshotId, item +: snapshotItems)
    }
    else {
      upsertSnapshot(snapshotId, snapshotItems)
      (item.snapshotId, item +: Nil)
    }
  }

  def ensureTablesCreated() {
    db.withSession {
      if(MTable.getTables(Balances.tableName).elements().isEmpty)
        Balances.ddl.create
    }
  }

  def upsertSnapshot(snapshotId: String, snapshotItems: Seq[Balance]) = {
    db.withSession {
      val delCount = Balances.where(_.snapshotId === snapshotId).delete
      log.debug(s"Deleted $delCount balances from snapshot $snapshotId")
      Balances.insertAll(snapshotItems: _*)
    }
  }


  val log = LoggerFactory.getLogger(classOf[BalancesLoader])
}
