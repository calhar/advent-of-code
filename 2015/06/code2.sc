sealed trait Command {
  def apply(lights: Vector[Vector[Int]]): Vector[Vector[Int]]
}

case class On(x1: Int, xLen: Int, y1: Int, yLen: Int) extends Command {
  def apply(lights: Vector[Vector[Int]]): Vector[Vector[Int]] =
    lights.patch(
      x1,
      lights.drop(x1).take(xLen).map(row =>
          row.patch(
            y1,
            row.drop(y1).take(yLen).map(_ + 1),
            yLen)
      ),
      xLen)
}

case class Off(x1: Int, xLen: Int, y1: Int, yLen: Int) extends Command {
  def apply(lights: Vector[Vector[Int]]): Vector[Vector[Int]] =
    lights.patch(
      x1,
      lights.drop(x1).take(xLen).map(row =>
          row.patch(
            y1,
            row.drop(y1).take(yLen).map(b => if (b != 0) b - 1 else 0),
            yLen)
      ),
      xLen)
}

case class Toggle(x1: Int, xLen: Int, y1: Int, yLen: Int) extends Command {
  def apply(lights: Vector[Vector[Int]]): Vector[Vector[Int]] =
    lights.patch(
      x1,
      lights.drop(x1).take(xLen).map(row =>
          row.patch(
            y1,
            row.drop(y1).take(yLen).map(_ + 2),
            yLen)
      ),
      xLen)
}

object Lights {
  def parseCommand(command: String): Command = {
    val onRegex = raw"turn on (\d+),(\d+) through (\d+),(\d+)".r
    val offRegex = raw"turn off (\d+),(\d+) through (\d+),(\d+)".r
    val toggleRegex = raw"toggle (\d+),(\d+) through (\d+),(\d+)".r

    command match {
      case onRegex(g1,g2,g3,g4) => {val (x1, y1, x2, y2) = (g1.toInt, g2.toInt, g3.toInt, g4.toInt)
                                    On(x1, x2 + 1- x1, y1, y2 + 1- y1)}
      case offRegex(g1,g2,g3,g4) => {val (x1, y1, x2, y2) = (g1.toInt, g2.toInt, g3.toInt, g4.toInt)
                                    Off(x1, x2 + 1- x1, y1, y2 + 1- y1)}
      case toggleRegex(g1,g2,g3,g4) => {val (x1, y1, x2, y2) = (g1.toInt, g2.toInt, g3.toInt, g4.toInt)
                                    Toggle(x1, x2 + 1 - x1, y1, y2 + 1- y1)}
    }
  }

  def run(config: List[String]) {
    val lights = config.map(parseCommand)
      .foldLeft(Vector.fill(1000)(Vector.fill(1000)(0)))(
        (lights, command) => command(lights)
      )
    println(lights.map(row => row.sum).sum)
  }

  def main(args: Array[String]) {
    val data = io.Source.stdin.getLines.toList

    run(data)
  }
}
