object Firewall {
  def severities(walls: List[(Int, Int)], delay: Int): List[Int] = {
    walls.map({ case (layer, range) => {
        val catchTime = (range - 1) * 2
        if ((layer + delay) % catchTime == 0) layer * range else 0
      }})
  }

  def caught(walls: List[(Int, Int)])(delay: Int): Boolean = {
    walls.map({ case (layer, range) => {
        val catchTime = (range - 1) * 2
        (layer + delay) % catchTime == 0
      }}).exists(_ == true)
  }

  def run(walls: List[(Int, Int)]) {
    println(severities(walls, 0).sum)
    val lowestDelay = Iterator.from(0).map(delay => caught(walls, delay)).indexOf(false)
    println(lowestDelay)
  }

  def main(args: Array[String]) {
    val data = scala.io.Source.stdin.getLines.map(line => {
        val wall = line.split(": ").map(_.toInt)
        (wall(0), wall(1))
      }).toList

    run(data)
  }
}
