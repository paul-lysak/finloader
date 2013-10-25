package finloader.loader

import scala.slick.jdbc.JdbcBackend.Database
import java.io.File
import java.net.URL
import com.github.tototoshi.csv.{CSVFormat, CSVReader}
import finloader.domain.{Income, Incomes}
//import scala.slick.driver.PostgresDriver.simple._
import Database.dynamicSession
import org.slf4j.LoggerFactory
import finloader.{DbUtils, FinloaderUtils}
import FinloaderUtils._


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
     val incomes = reader.toStream() match {
       case firstRow #:: body =>
         val p = firstRow.zipWithIndex.toMap
         for(row <- body) yield {
           val r = row.toIndexedSeq
           val (amt, curr) = parseAmount(r(p("amount")))
           count += 1
           Income(id = idPrefix+r(p("id")),
             date = parseDate(r(p("date"))),
             amount = amt,
             currency = curr,
             source = r(p("source")),
             comment = r(p("comment")))
         }
       case _ =>
         log.error("can't find first line")
         Stream()
     }

    lazy val defaultedIncomes: Stream[Income] = (Income(null, null, 0, null, null, null) #:: defaultedIncomes).zip(incomes).
          map({case (prev, curr) =>
        val date = if(curr.date == null) prev.date else curr.date
        curr.copy(date = date)})

     defaultedIncomes.foreach(upsert)

     log.info(s"Loaded $count incomes from $source")
   }

   def ensureTablesCreated() = ??? //ensureTableCreated(Incomes)

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
