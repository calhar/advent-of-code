#!/usr/bin/env scala

import java.time._
import java.time.format.DateTimeFormatter

object Start extends App {
  val adventZone = ZoneId.of("America/New_York")
  val adventTime = LocalDateTime.now(adventZone)

  val subDir = adventTime.format(DateTimeFormatter.ofPattern("yyyy/dd"))

  val dir = new java.io.File(subDir)

  dir.mkdirs
  new java.io.File(dir.getAbsolutePath + "/code.sc").createNewFile
  new java.io.File(dir.getAbsolutePath + "/input.txt").createNewFile
}
