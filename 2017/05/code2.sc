object Jumps {
  def run(instList: List[Int]) {
    val instructions = new Array[Int](instList.length)
    instList.copyToArray(instructions)

    var acc = 0
    val jumps = Iterator.continually(0).map((_) => {
      val jump = instructions(acc)
      instructions(acc) += (if (jump >= 3) -1 else 1)
      acc += jump
      acc
    }).indexWhere((inst) => inst >= instructions.length || inst < 0)

    println(jumps + 1)
  }

  def main(args: Array[String]) {
    val data = scala.io.Source.stdin.getLines

    val instList = data.map((line) =>
        line.toInt).toList
    
    run(instList)
  }
}
