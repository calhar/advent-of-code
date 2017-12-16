object Generator {
  def apply(factor: Int, seed: Int, filter: Int): Generator =
    new Generator(factor, seed, filter)
}

class Generator(private val factor: Int,
                private val seed: Int,
                private val filter: Int) extends Iterator[Int]{
  private var value: Long = seed
  def next(): Int = {
    do {
      value = (value * factor) % Int.MaxValue
    } while ((value & (filter - 1)) != 0)
    value.toInt
  }

  def hasNext(): Boolean = true
}
  
object Judge {
  def run(seeds: List[Int]) {
    val matches1 = Generator(16807, seeds(0), 1)
      .zip(Generator(48271, seeds(1), 1)).take(4e7.toInt).map(pair =>
        (pair._1 & 65535) == (pair._2 & 65535)
      ).count(_ == true)

    println(matches1)

    val matches2 = Generator(16807, seeds(0), 4)
      .zip(Generator(48271, seeds(1), 8)).take(5e6.toInt).map(pair =>
        (pair._1 & 65535) == (pair._2 & 65535)
      ).count(_ == true)

    println(matches2)
  }

  def main(args: Array[String]) {
    val data = scala.io.Source.stdin.getLines.map(
      line => line.split(" ").last.toInt
    ).toList

    run(List(65, 8921))
    run(data)
  }
}
