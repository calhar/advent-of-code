object Frequency {

  def run(data: List[String]) {
    val frequencies = data.map((s: String) => s.toInt)
    println(frequencies.sum)
  }

  def main(args: Array[String]) {
    val data = scala.io.Source.stdin.getLines.toList
    run(data)
  }
}
