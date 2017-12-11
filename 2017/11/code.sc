object Hexes {
  def run(dirs: List[String]) {
    val point = dirs.foldLeft((0, 0, 0))((point, dir) => dir match {
      case "n" => (point._1, point._2 + 1, point._3 - 1)
      case "ne" => (point._1 + 1, point._2, point._3 - 1)
      case "nw" => (point._1 - 1, point._2 + 1, point._3)
      case "s" => (point._1, point._2 - 1, point._3 + 1)
      case "sw" => (point._1 - 1, point._2, point._3 + 1)
      case "se" => (point._1 + 1, point._2 - 1, point._3)
    })

    val dist = List(math.abs(point._1), math.abs(point._2), math.abs(point._3)).max
    println(dist)
  }

  def main(args: Array[String]) {
    val data = scala.io.Source.stdin.getLines.next.split(",").toList

    run(data)
  }
}
