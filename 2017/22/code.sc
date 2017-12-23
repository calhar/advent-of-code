case class MapState(val map: Map[(Int, Int), Boolean],
                    val pos: (Int, Int),
                    val dir: (Int, Int))

object Virus {
  implicit class TupleExtend(t: (Int, Int)) {
    import Numeric.Implicits._

    def +(p: (Int, Int)) = (t._1 + p._1, t._2 + p._2)
    def turnLeft = (-1 * t._2, t._1)
    def turnRight = (t._2, -1 * t._1)
  }

  def activity(state: MapState, bursts: Int): (MapState, Int) = {
    if (state.map(state.pos)) {
      (MapState(state.map + (state.pos -> false),
                state.pos + state.dir.turnRight,
                state.dir.turnRight),
       bursts)
    } else {
      (MapState(state.map + (state.pos -> true),
                state.pos + state.dir.turnLeft,
                state.dir.turnLeft),
       bursts + 1)
    }
  }

  def run(in: List[String]) {
    val (xOff, yOff) = (in.length / 2, in.head.length / 2)
    val boolMap = in.zipWithIndex.flatMap({
      case (row, idx) => row.zipWithIndex.map({
        case (c, idy) => (idx - xOff, idy - yOff) -> (c == '#')
      })
    }).toMap.withDefaultValue(false)

    val (mapState, bursts) = Iterator.iterate((MapState(boolMap, (0, 0), (-1, 0)), 0))({
      case (map, bursts) => {
        activity(map, bursts)
      }
    }).drop(10000).next

    println(bursts)
  }

  def main(args: Array[String]) {
    val data = scala.io.Source.stdin.getLines.toList

    run(data)
  }
}
