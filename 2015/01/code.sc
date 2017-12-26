object Parser {
  def run(instructions: String) {
    val floor = instructions.count(_ == '(') - instructions.count(_ == ')')
    println(floor)

    val basement = instructions.scanLeft(0)(
      (acc, c) => if (c == '(') acc + 1 else acc - 1
      ).indexWhere(_ < 0)
    println(basement)
  }

  def main(args: Array[String]) {
    val data = io.Source.stdin.getLines.next

    run(data)
  }
}
