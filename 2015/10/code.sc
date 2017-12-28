object LookSay {
  val groupRegex = raw"""((\d)\2*)""".r
  def lookAndSay(s: String): String = {
    groupRegex.findAllIn(s).map(g => "" + g.length + g.head)
    .mkString
  }

  def run(input: String) {
    val sequence = Stream.iterate(input)(s => lookAndSay(s))(40)

    println(sequence.length)
    val sequence2 = Stream.iterate(input)(lookAndSay)(50)
    println(sequence2.length)
  }

  def main(args: Array[String]) {
    val data = io.Source.stdin.getLines.next

    run(data)
  }
}
