import scala.collection.mutable.Map


object Memory {
  def indToPoint(index: Int): (Int, Int) = {
    val ring = math.floor((math.floor(math.sqrt(index - 1)) + 1) / 2).toInt

    val point = if (ring == 0) (0, 0)
      else {
        val length = 2 * ring

        val innerElements = math.pow(2 * ring - 1, 2).toInt

        val remaining = index - innerElements

        val sideInd = ((remaining - 1) % length) + 1

        val side = (remaining - 1) / length
        side match {
          case 0 => (ring, ring - sideInd)
          case 1 => (ring - sideInd, -ring)
          case 2 => (-ring, sideInd - ring)
          case 3 => (sideInd - ring, ring)
        }
      }
    point
  }

  def pointToInd(point: (Int, Int)): Int = {
    point._1 + point._2
  }

  def run(square: Int) {
    val inds = Stream.from(1)

    val points = inds.map(indToPoint)

    val values = Map[(Int, Int), Int]((0,0) -> 1).withDefaultValue(0)

    val neighbours = List((-1, -1), (0, -1), (1, -1),
                          (-1, 0),           (1, 0),
                          (-1, 1),  (0, 1),  (1, 1))

    val summation = points.map({ case (x, y) =>
      val ourVal = values((x, y))
      neighbours.foreach({ case (a, b) =>
        values((x + a, y + b)) += ourVal
      })
      ourVal
    })

    println(summation.find(_ > square).get)
  }

  def main(args: Array[String]) {
    val data = scala.io.Source.stdin.getLines.mkString.toInt

    run(data)
  }
}
