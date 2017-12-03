class Folder() {
  var dir = (0, 1)
  var pos = (0, -1)
  var elements = 0

  def apply(length: Int): List[Tuple2[Int, Int]] = {
    var (x, y) = pos
    var (dx, dy) = dir

    val points = for (_ <- (0 until length)) yield { x = x + dx; y = y + dy; (x, y) }

    pos = (x, y)
    dir = (dy, -dx)

    points.toList
  }
}

object Memory {
  def run(square: Int) {
    val fold = new Folder()

    val folds = Stream.from(1)
        .flatMap((x)=> List(x, x))

    var (x, y) = folds.flatMap((len) => fold(len)).take(square).last
    println(math.abs(x) + math.abs(y))
  }

  def main(args: Array[String]) {
    val data = scala.io.Source.stdin.getLines.mkString.toInt

    run(data)
  }
}
