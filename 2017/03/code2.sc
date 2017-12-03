import scala.collection.mutable.ArrayBuffer

class SpiralPointFolder() {
  var dir = (0, 1)
  var pos = (0, 0)

  def apply(length: Int): List[Tuple2[Int, Int]] = {
    var (x, y) = pos
    var (dx, dy) = dir

    val points = for (_ <- (0 until length)) yield { 
      val now = (x, y); x = x + dx; y = y + dy; now }

    pos = (x, y)
    dir = (dy, -dx)

    points.toList
  }
}

class SpiralFolder() {
  var dir = (0, 1)
  var pos = (0, 0)
  var curElement = 0

  var neighbourVals= ArrayBuffer(ArrayBuffer(1))

  def apply(length: Int): List[Tuple2[Int, Int]] = {
    var (x, y) = pos
    var (dx, dy) = dir

    val points = for (_ <- (0 until length)) yield { 
      val now = (x, y); x = x + dx; y = y + dy; now }

    pos = (x, y)
    dir = (dy, -dx)

    points.toList
  }

  def apply(length: Int, points: Stream[Tuple2[Int, Int]]): List[Int] = {
    var (x, y) = pos
    var (dx, dy) = dir

    val checks = for (_ <- (0 until length)) yield {
      val neighbourIndexes = for (i <- -1 to 1; j <- -1 to 1 if (i, j) != (0, 0)) yield {
        points.indexOf((x + i, y + j))
      }

      val ourValue = neighbourVals.remove(0).sum

      neighbourVals ++= (for (_ <- (neighbourVals.size + 1) to neighbourIndexes.max)
        yield { ArrayBuffer[Int]() })

      neighbourIndexes.filter(_ > curElement).foreach((ind) => {
        neighbourVals((ind - 1) - curElement) += ourValue
      })

      x = x + dx
      y = y + dy
      curElement += 1
      ourValue
    }

    pos = (x, y)
    dir = (dy, -dx)
    checks.toList
  }
}

object Memory {
  def run(square: Int) {
    val pointFold = new SpiralPointFolder()
    val spiral = new SpiralFolder()

    val folds = Stream.from(1)
        .flatMap((x)=> List(x, x))

    val points = folds.flatMap((len) => pointFold(len))

    val first = folds.flatMap((len) => spiral(len, points)).find(_ > square).head
    println(first)
  }

  def main(args: Array[String]) {
    val data = scala.io.Source.stdin.getLines.mkString.toInt

    run(data)
  }
}
