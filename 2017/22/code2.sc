case class MapState(val map: Map[(Int, Int), Char],
                    val pos: (Int, Int),
                    val dir: (Int, Int))

object Virus {
  implicit class TupleExtend(t: (Int, Int)) {
    import Numeric.Implicits._

    def +(p: (Int, Int)) = (t._1 + p._1, t._2 + p._2)
    def turnLeft = (-1 * t._2, t._1)
    def turnRight = (t._2, -1 * t._1)
    def reverse = (-1 * t._1, -1 * t._2)
  }

  def activity(state: MapState, bursts: Int): (MapState, Int) = {
    state.map(state.pos) match {
      case '#' => (MapState(state.map + (state.pos -> 'F'),
                            state.pos + state.dir.turnRight,
                            state.dir.turnRight),
                   bursts)
      case 'W' => (MapState(state.map + (state.pos -> '#'),
                            state.pos + state.dir,
                            state.dir),
                   bursts + 1)
      case 'F' => (MapState(state.map + (state.pos -> '.'),
                            state.pos + state.dir.reverse,
                            state.dir.reverse),
                   bursts)
      case '.' => (MapState(state.map + (state.pos -> 'W'),
                            state.pos + state.dir.turnLeft,
                            state.dir.turnLeft),
                   bursts)
    }
  }

  def run(in: List[String]) {
    val (xOff, yOff) = (in.length / 2, in.head.length / 2)
    val initMap = in.zipWithIndex.flatMap({
      case (row, idx) => row.zipWithIndex.map({
        case (c, idy) => (idx - xOff, idy - yOff) -> c
      })
    }).toMap.withDefaultValue('.')

    val (mapState, bursts) = Iterator.iterate((MapState(initMap, (0, 0), (-1, 0)), 0))({
      case (map, bursts) => {
        activity(map, bursts)
      }
    }).drop(10000000).next

    println(bursts)
  }

  def main(args: Array[String]) {
    val data = scala.io.Source.stdin.getLines.toList

    run(data)
  }
}
