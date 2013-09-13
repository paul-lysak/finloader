package finloader.loader

import scala.slick.session.Database
import java.io.File
import java.net.URL
import com.github.tototoshi.csv.{CSVFormat, CSVReader}
import finloader.domain.{Income, Incomes, Expenses, Expense}
import scala.slick.driver.PostgresDriver.simple._
import Database.threadLocalSession
import org.joda.time.format.ISODateTimeFormat
import scala.slick.jdbc.meta.MTable
import org.slf4j.LoggerFactory
import finloader.{DbUtils, FinloaderUtils}


/**
  * @author Paul Lysak
  *         Date: 05.07.13
  *         Time: 21:55
  */
class IncomesLoader(db: Database)(implicit csvFormat: CSVFormat) extends DataLoader with DbUtils {
   private implicit val dbImpl = db

   def load(source: URL, idPrefix: String = "") {
     log.info(s"Loading incomes from $source")
     log.debug(s"Using CSV separator ${csvFormat.separator}")
     val reader = CSVReader.open(new File(source.toURI))
     var count = 0
     reader.toStream() match {
       case firstRow #:: body =>
         val p = firstRow.zipWithIndex.toMap
         for(row <- body) {
           val r = row.toIndexedSeq
           val (amt, curr) = FinloaderUtils.parseAmount(r(p("amount")))
           val income = Income(id = idPrefix+r(p("id")),
             date = ISODateTimeFormat.date().parseLocalDate(r(p("date"))),
             amount = amt,
             currency = curr,
             source = r(p("source")),
             comment = r(p("comment")))
           upsert(income)
           count += 1
         }
       case _ =>
         log.error("can't find first line")
     }
     log.info(s"Loaded $count incomes from $source")
   }

   def ensureTablesCreated() = ensureTableCreated(Incomes)

   private def upsert(income: Income) {
     db.withSession {
       Query(Incomes).map(_.id).filter(_ === income.id).firstOption() match {
         case Some(existingId) => {
           log.debug(s"Update income: $existingId")
           Incomes.where(_.id === existingId).update(income)
         }
         case None => {
           Incomes.insert(income)
         }
       }
     }
   }//end upsert

   val log = LoggerFactory.getLogger(classOf[IncomesLoader])
 }
