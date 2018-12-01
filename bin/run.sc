#!/usr/bin/env scala

import java.time._
import java.time.format.DateTimeFormatter

object Start extends App {
  val subDir = if (args.length == 0) {
    val adventZone = ZoneId.of("America/New_York")
    val adventTime = LocalDateTime.now(adventZone)

    adventTime.format(DateTimeFormatter.ofPattern("yyyy/dd"))
  } else {
    args(0)
  }

  val dir = new java.io.File(subDir)

  dir.mkdirs
  new java.io.File(dir.getPath+ "/code.sc").createNewFile
  new java.io.File(dir.getPath+ "/input.txt").createNewFile
}
