object Fabric {

  def parse(str: String): List[Int] = {
    val regex = raw"#(\d+) @ (\d+),(\d+): (\d+)x(\d+)".r

    val regex(id, startX, startY, width, height) = str

    List[Int](id.toInt, startX.toInt, startY.toInt, width.toInt, height.toInt)
  }

  def run(data: List[String]) {
    val claims = data.map((s: String) => parse(s))
    val claimed = claims.foldLeft(Vector.fill(1000, 1000)( 0 ))((map, claim) => {
      val id :: startX :: startY :: width :: height :: _ = claim
      val colSlice = map.slice(startX, startX + width).map((col) => {
        col.patch(startY,
          col.slice(startY, startY + height).map((inch) => inch + 1),
          height)
      })
      map.patch(startX, colSlice, width)
    })
    val squares = claimed.map((col: Vector[Int]) => col.count((inch) => inch > 1)).sum
    println(squares)
  }

  def main(args: Array[String]) {
    val data = scala.io.Source.stdin.getLines.toList
    run(data)
  }
}
