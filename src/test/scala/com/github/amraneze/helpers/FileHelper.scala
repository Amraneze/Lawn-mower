package com.github.amraneze.helpers

import java.io.{ File, FileNotFoundException, PrintWriter }

object FileHelper {
  def writeToTempFile(text: String,
                     prefix: Option[String] = None,
                     suffix: Option[String] = None): File = {
    val tempFile =
      File.createTempFile(prefix.getOrElse("prefix-"), suffix.getOrElse("-suffix"))
    tempFile.deleteOnExit()
    new PrintWriter(tempFile) {
      try {
        write(text)
      } catch {
        case exception: FileNotFoundException =>
          println(
            s"Error: File not found, check folder's permissions ${exception.getMessage}"
          )
        case _: Throwable =>
          println("Error: An issue occurred while writing to the temporary file.")
      } finally {
        close()
      }
    }
    tempFile
  }
}
