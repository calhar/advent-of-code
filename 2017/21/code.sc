object Fractals {
  type Pattern = List[String]

  def splitImage(image: Pattern, size: Int): List[List[Pattern]] = {
    image.map(_.grouped(size).toList).grouped(size).toList.map(_.transpose)
  }

  def collapse(grid: List[List[Pattern]]): Pattern = {
    grid.map(_.transpose).flatten.mkString
  }

  def symmetries(pattern: Pattern): List[Pattern] = {
    val rotated = List(pattern,
                       pattern.transpose.reverse.map(_.mkString),
                       pattern.reverse.map(_.reverse),
                       pattern.transpose.map(_.reverse.mkString))
    rotated.map(p => List(p, p.reverse)).flatten
  }

  def expandImage(image: Pattern,
                  rules: Map[Pattern, Pattern]): Pattern = {
    val split = if (image.length % 2 == 0) splitImage(image, 2)
                else splitImage(image, 3)
    val newGrid = split.map(
      patternRow => patternRow.map(
        pattern => rules(pattern)
      )
    )

    collapse(newGrid)
  }

  def run(compressedRules: Iterator[String]) {
    val rules: Map[Pattern, Pattern] = compressedRules.flatMap(
      rule => {
        val parts = rule.split(" => ")
        val pattern = parts(0).split("/").toList
        val matched = parts(1).split("/").toList
        symmetries(pattern).map(symmetry => symmetry -> matched)
      }
    ).toMap

    val start: Pattern = List(".#.","..#","###")
    val pictures = Stream.iterate(start)(image => expandImage(image, rules))

    println(pictures(5).mkString.count(_ == '#'))
    println(pictures(18).mkString.count(_ == '#'))
  }

  def main(args: Array[String]) {
    val data = scala.io.Source.stdin.getLines

    run(data)
  }
}
