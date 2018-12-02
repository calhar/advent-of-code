object Hamming {
  def run(data: List[String]) {
    val idPair = data.combinations(2).find((pair) =>
      pair(0).zip(pair(1)).scanLeft(0)((acc, chars) =>
        acc + (if (chars._1 != chars._2) 1 else 0)
      ).find((count) => count == 2).isEmpty
    ).get

    val commonChars = idPair(0).zip(idPair(1)).filter((chars) =>
        chars._1 == chars._2
      ).map((chars) => chars._1).mkString

    println(commonChars)
  }

  def main(args: Array[String]) {
    val data = scala.io.Source.stdin.getLines.toList
    run(data)
  }
}
