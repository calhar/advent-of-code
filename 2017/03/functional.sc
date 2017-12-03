class SpiralPointFolder() {
  var dir = (0, 1)
  var pos = (0, 0)
  var elements = 0

  def apply(length: Int): List[Tuple2[Int, Int]] = {
    var (x, y) = pos
    var (dx, dy) = dir

    val points = for (_ <- (0 until length)) yield { 
      val now = (x, y)
      x = x + dx
      y = y + dy
      now
    }

    pos = (x, y)
    dir = (dy, -dx)

    points.toList
  }
}

object Memory {
  def run(square: Int) {
    val fold = new SpiralPointFolder()

    val folds = Stream.from(1)
        .flatMap((x)=> List(x, x))

    val points = folds.flatMap((len) => fold(len)).take(square)

    val (x, y) = points.last
    println(math.abs(x) + math.abs(y))
  }

  def main(args: Array[String]) {
    val data = scala.io.Source.stdin.getLines.mkString.toInt

    run(data)
  }
}
