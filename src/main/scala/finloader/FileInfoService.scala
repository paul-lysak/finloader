package finloader

import org.joda.time.LocalDateTime

/**
 * Created by gefox on 26.12.14.
 */
class FileInfoService {
  def needsUpdate(fileCode: String, dateTime: LocalDateTime): Boolean = {
    true
  }

  def setUpdatedDateTime(fileCode: String, dateTime: LocalDateTime): Unit = {

  }

}
