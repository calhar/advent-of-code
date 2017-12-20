case class MapState(val active: Boolean,
                    val location: (Int, Int),
                    val direction: (Int, Int),
                    val curSym: Char,
                    val letters: String)

object Tubes {
  implicit class Tuple2Arithmetic[A: Numeric, B: Numeric](t: (A, B)) {
    import Numeric.Implicits._

    def +(p: (A, B)) = (t._1 + p._1, t._2 + p._2)
    def -(p: (A, B)) = (t._1 - p._1, t._2 - p._2)
  }

  def checkBounds(loc: (Int, Int), map: Vector[String]): Boolean = {
    loc._1 >= 0 && loc._2 >= 0 && loc._1 < map.length && loc._2 < map(loc._1).length
  }

  def cornerDirection(loc: (Int, Int),
                      dir: (Int, Int),
                      map: Vector[String]): (Int, Int) = {
    val neighbours = Set((-1, 0), (0, -1), (0, 1), (1, 0))

    (neighbours &~ Set((0, 0) - dir, dir))
      .find(newDir => {
        val newLoc = newDir + loc
       map(newLoc._1)(newLoc._2) != ' '
    }).get
  }

  def traverse(packet: MapState, map: Vector[String]): MapState = {
    val newLoc = packet.location + packet.direction
    
    val newSym =
      if (checkBounds(newLoc, map)) map(newLoc._1)(newLoc._2)
      else packet.curSym

    val (active, newDir, letters): (Boolean, (Int, Int), String) = if (newSym != packet.curSym) {
      newSym match {
        case '+' => (true, cornerDirection(newLoc, packet.direction, map), packet.letters)
        case c if c == '-' || c == '|' => (true, packet.direction, packet.letters)
        case c if c.isLetter => (true, packet.direction, packet.letters + c)
        case ' ' => (false, packet.direction, packet.letters)
      }
    } else (true, packet.direction, packet.letters)
    MapState(active, newLoc, newDir, newSym, letters)
  }

  def run(map: Vector[String]) {
    val start = (0, map.head.indexWhere(_ != ' '))
    val startDir = (1, 0)
    val startStr = if (map(0)(start._2).isLetter) map(0)(start._2).toString
                   else ""

    val packetStream = 
      Stream.iterate(MapState(true, start, startDir, map(0)(start._2), startStr))(
        packet => traverse(packet, map)
      )

    val endIdx = packetStream.indexWhere(packet => !packet.active)
    println(packetStream(endIdx).letters)
    println(endIdx)
  }

  def main(args: Array[String]) {
    val data = scala.io.Source.stdin.getLines.toVector

    run(data)
  }
}
