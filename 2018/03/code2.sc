object Fabric {

  def parse(str: String): List[Int] = {
    val regex = raw"#(\d+) @ (\d+),(\d+): (\d+)x(\d+)".r

    val regex(id, startX, startY, width, height) = str

    List[Int](id.toInt, startX.toInt, startY.toInt, width.toInt, height.toInt)
  }

  def overlap(claim1: List[Int], claim2: List[Int]): Boolean = {
    val id1 :: startX1 :: startY1 :: width1 :: height1 :: _ = claim1
    val id2 :: startX2 :: startY2 :: width2 :: height2 :: _ = claim2

    !(startX1 > startX2 + width2 || startX2 > startX1 + width1) && 
    !(startY1 > startY2 + height2 || startY2 > startY1 + height1)
  }

  def run(data: List[String]) {
    val claims = data.map((s: String) => parse(s))
    val trueClaim = claims.find((claim) => {
      claims.scanLeft(0)((acc, counterClaim) => {
        if (overlap(claim, counterClaim)) acc + 1 else acc
      }).find((count) => count == 2).isEmpty
    }).get
    println(trueClaim(0))
  }

  def main(args: Array[String]) {
    val data = scala.io.Source.stdin.getLines.toList
    run(data)
  }
}
