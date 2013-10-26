package finloader.loader

import com.github.tototoshi.csv.{CSVReader, CSVFormat}
import java.net.URL
import org.slf4j.LoggerFactory
import finloader.domain.{Balance, Balances}
import scala.slick.jdbc.JdbcBackend.Database
import scala.slick.driver.JdbcDriver.simple._
import scala.slick.lifted.TableQuery
import Database.dynamicSession
import java.io.File
import finloader.{DbUtils, FinloaderUtils}
import finloader.FinloaderUtils._

/**
 * @author Paul Lysak
 *         Date: 15.08.13
 *         Time: 21:43
 */
class BalancesLoader(db: Database)(implicit csvFormat: CSVFormat) extends DataLoader with DbUtils {
  private implicit val dbImpl = db

  def load(source: URL, idPrefix: String) {
    log.info(s"Loading balances from $source")
    log.debug(s"Using CSV separator ${csvFormat.separator}")
    val reader = CSVReader.open(new File(source.toURI))
    var count = 0
    val balances = reader.toStream() match {
      case firstRow #:: body =>
        val p = firstRow.zipWithIndex.toMap
        for(row <- body) yield {
          val r = row.toIndexedSeq
          val (amt, curr) = FinloaderUtils.parseAmount(r(p("amount")))
          count += 1
          Balance(id = idPrefix+count,
            snapshotId = idPrefix+r(p("snapshotId")),
            date = parseDate(r(p("date"))),
            place = r(p("place")),
            amount = amt,
            currency = curr,
            comment = r(p("comment")))
        }
      case _ =>
        log.error("can't find first line")
        Stream()
    }

    lazy val defaultedBalances: Stream[Balance] = (Balance(null, null, null, null, 0, null) #:: defaultedBalances).zip(balances).
      map({case (prev, current) =>
        val snapshotId = if(current.snapshotId == idPrefix) prev.snapshotId else current.snapshotId
        val date = if(current.date == null) prev.date else current.date
        current.copy(snapshotId = snapshotId, date = date)
    })

    defaultedBalances.foreach(println)

    val groupedBalances = groupBalances(defaultedBalances)

    groupedBalances.foreach((upsertSnapshot _).tupled)

    log.info(s"Loaded $count balances from $source")
  }

  def ensureTablesCreated() = ensureTableCreated(TableQuery[Balances])

  def upsertSnapshot(snapshotId: String, snapshotItems: Seq[Balance]) = {
    db.withDynSession {
      val balQuery = TableQuery[Balances]
      val delCount = balQuery.where(_.snapshotId === snapshotId).delete
      log.debug(s"Deleted $delCount balances from snapshot $snapshotId")
      balQuery.insertAll(snapshotItems: _*)
    }
  }

  private def groupBalances(balances: Stream[Balance]): Stream[(String, Seq[Balance])] = {
    if(balances.isEmpty)
      Stream()
    else {
      val snapshotId = balances.head.snapshotId
      val (thisSnapshot, remainder) = balances.span(_.snapshotId == snapshotId)
      (snapshotId, thisSnapshot.toSeq) #:: groupBalances(remainder)
    }
  }

  val log = LoggerFactory.getLogger(classOf[BalancesLoader])
}
