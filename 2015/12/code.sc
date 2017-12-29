object Accounting {
  def run(json: String) {
    val numberRegex = raw"([-+]?\d+)".r
    val numbers = numberRegex.findAllIn(json).map(_.toInt)

    println(numbers.sum)
  }

  def main(args: Array[String]) {
    val data = io.Source.stdin.getLines.next

    run(data)
  }
}
