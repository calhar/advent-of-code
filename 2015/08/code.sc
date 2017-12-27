object Matchsticks {
  val escapeRegex = raw"""\\"|\\\\|\\x[0-9a-f]{2}""".r
  val encodeRegex = raw""""|\\""".r

  def diskLength(s: String): Int = {
    s.length - escapeRegex.findAllIn(s).map(escape => escape.length - 1).sum
  }

  def encodeLength(s: String): Int = {
    s.length + encodeRegex.findAllIn(s).map(c => 1).sum + 2
  }

  def run(santasList: List[String]) {
    val lenDiffs = santasList.map(s => s.length - diskLength(s.slice(1, s.length - 1)))

    println(lenDiffs.sum)

    val encodeDiffs = santasList.map(s => encodeLength(s) - s.length)
    println(encodeDiffs.sum)
  }

  def main(args: Array[String]) {
    val data = io.Source.stdin.getLines.toList

    run(data)
  }
}
