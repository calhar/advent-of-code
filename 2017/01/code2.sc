import scala.annotation.tailrec
object Captcha2 {

  def run(data: String) {
    @tailrec
    def check(left: String, right: String, accumulator: List[Char]): List[Char] =
     left  match {
        case ""=> accumulator
        case w => check(left.tail, right.tail,
          if (left.head == right.head) left.head :: right.head :: accumulator else accumulator)
      }


    val (l, r) = data.splitAt(data.length / 2)
    val duplicates = check(l, r, Nil)
    val digitSum =
      duplicates.map((c: Char) => c.asDigit).foldLeft(0)((m, n) => m + n)
    println(digitSum)
  }

  def main(args: Array[String]) {
    val data = scala.io.Source.stdin.getLines.mkString
    run(data)
  }
}
