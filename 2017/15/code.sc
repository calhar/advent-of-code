class Generator(private val factor: Int, private val seed: Int) {
  private var value: Long = seed
  def next(): Int = {
    value = (value * factor) % Int.MaxValue
    value.toInt
  }
}
  
object Judge {
  def run(seeds: List[Int]) {
    val genA = new Generator(16807, seeds(0))
    val genB = new Generator(48271, seeds(1))

    val matches = (0 until 4e7.toInt).map(_ =>
      (genA.next & 65535) == (genB.next & 65535)
    ).count(_ == true)

    println(matches)
  }

  def main(args: Array[String]) {
    val data = scala.io.Source.stdin.getLines.map(
      line => line.split(" ").last.toInt
    ).toList

    run(List(65, 8921))
    run(data)
  }
}
