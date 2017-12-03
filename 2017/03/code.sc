object Memory {
  def run(square: Int) {
    val ring = math.floor((math.floor(math.sqrt(square - 1)) + 1) / 2).toInt
    val length = 2 * ring

    val innerElements = math.pow(2 * ring - 1, 2).toInt

    val remaining = square - innerElements

    val sideInd = ((remaining - 1) % length) + 1

    println("----")
    println(length)
    println(innerElements)
    println(remaining)
    println(sideInd)
    println(ring + math.abs(sideInd - ring))

  }

  def main(args: Array[String]) {
    val data = scala.io.Source.stdin.getLines.mkString.toInt

    run(data)
  }
}
