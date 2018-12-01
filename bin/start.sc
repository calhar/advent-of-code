#!/bin/sh
exec scala -classpath "bin" "$0" "$@"
!#

import java.time.{LocalDate, ZoneId}
import java.time.format.DateTimeFormatterBuilder
import java.time.temporal.ChronoField
import java.io.File
import java.io.BufferedWriter
import java.io.FileWriter

object Start extends App {
  val dtf = (new DateTimeFormatterBuilder())
      .appendPattern("yyyy/dd")
      .parseDefaulting(ChronoField.MONTH_OF_YEAR, 12)
      .toFormatter

  val adventTime = if (args.length == 0) {
    val adventZone = ZoneId.of("America/New_York")
    LocalDate.now(adventZone)
  } else {
    LocalDate.parse(args(0), dtf)
  }

  val subDir = adventTime.format(dtf)
  (new File(subDir)).mkdirs
  (new File(subDir + "/code.sc")).createNewFile

  val inputFile = new File(subDir + "/input.txt")
  inputFile.createNewFile

  val inputURL = "https://adventofcode.com/" +
      adventTime.getYear +
      "/day/" +
      adventTime.getDayOfMonth +
      "/input"

  val cookie = scala.io.Source.fromFile("session").getLines.mkString

  val inputData = Downloader.get(inputURL, cookie)

  val writer = new BufferedWriter(new FileWriter(inputFile))
  writer.write(inputData)
  writer.close
}
