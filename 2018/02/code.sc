import scala.language.implicitConversions

object Checksum {
  implicit def bool2int(b: Boolean) = if (b) 1 else 0

  def run(data: List[String]) {
    val checksum = data.map((s: String) => {
      val counts = s.foldLeft(Map[Char, Int]().withDefaultValue(0))((acc, c) => {
        acc + (c -> (acc(c) + 1))
      }).values.toSet
      (counts(2), counts(3))
    }).foldLeft((0, 0))((acc, check) =>
      (acc._1 + check._1, acc._2 + check._2)
    )
    println(checksum._1 * checksum._2)
  }

  def main(args: Array[String]) {
    val data = scala.io.Source.stdin.getLines.toList
    run(data)
  }
}
