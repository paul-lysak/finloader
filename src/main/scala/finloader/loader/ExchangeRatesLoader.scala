package finloader.loader

import java.net.URL
import finloader.domain.{ExchangeRate, ExchangeRates}
import scala.slick.lifted.TableQuery
import scala.slick.jdbc.JdbcBackend.Database
import scala.slick.driver.JdbcDriver.simple._
import finloader.{FinloaderUtils, DbUtils}
import com.github.tototoshi.csv.{CSVReader, CSVFormat}
import org.slf4j.LoggerFactory
import java.io.File

import finloader.FinloaderUtils._

/**
 * @author Paul Lysak
 *         Date: 07.02.14
 *         Time: 22:34
 */
class ExchangeRatesLoader(db: Database)(implicit csvFormat: CSVFormat) extends DataLoader with DbUtils {
  private implicit val dbImpl = db

  def load(source: URL, idPrefix: String) = {
    log.info(s"Loading exchange rates from $source")
    log.debug(s"Using CSV separator ${csvFormat.separator}")
    val reader = CSVReader.open(new File(source.toURI))
    var count = 0
    val ratesStream: Stream[ExchangeRate] =  (reader.toStream() match {
      case firstRow #:: body =>
        val p = firstRow.zipWithIndex.toMap

        for(row <- body) yield {
          val r = row.toIndexedSeq
          count += 1
          ExchangeRate(id = idPrefix+r(p("id")),
            date = parseDate(r(p("date"))),
            currency = r(p("currency")),
            rate = BigDecimal(r(p("rate"))),
            comment = r(p("comment")))
        }
      case _ =>
        log.error("can't find first line")
        Stream()
    })

    lazy val defaultedRates: Stream[ExchangeRate] = (ExchangeRate(null, null, null, 0, null) #:: defaultedRates).zip(ratesStream).
          map({case (prev, curr) =>
        val date = if(curr.date == null) prev.date else curr.date
        curr.copy(date = date)
    })

    defaultedRates.foreach(upsert)

    log.info(s"Loaded $count exchange rates from $source")
  }

  def ensureTablesCreated() = ensureTableCreated(TableQuery[ExchangeRates])

  private def upsert(rate: ExchangeRate) {
    db.withSession {
      implicit session =>
      val expQuery = TableQuery[ExchangeRates]
      expQuery.map(_.id).filter(_ === rate.id).firstOption() match {
        case Some(existingId) => {
          log.debug(s"Update $existingId")
          expQuery.where(_.id === existingId).update(rate)
        }
        case None => {
          expQuery.insert(rate)
        }
      }
    }
  }//end upsert

  val log = LoggerFactory.getLogger(classOf[ExchangeRatesLoader])
}
