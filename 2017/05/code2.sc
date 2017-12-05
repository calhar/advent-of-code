object Jumps {
  def run(instructions: Array[Int]) {
    var acc = 0
    val jumps = Iterator.continually(0).map((_) => {
      val jump = instructions(acc)
      instructions(acc) += (if (jump >= 3) -1 else 1)
      acc += jump
      acc
    }).indexWhere(_ >= instructions.length)

    println(jumps + 1)
  }

  def main(args: Array[String]) {
    val data = scala.io.Source.stdin.getLines

    val intData = data.map((line) =>
        line.toInt).toList

    val instructions = new Array[Int](intData.length)
    intData.copyToArray(instructions)

    run(Array(0, 3, 0, 1, -3))
    run(instructions)
  }
}
