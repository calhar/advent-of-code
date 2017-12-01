import scala.annotation.tailrec
object Captcha {

  def run(data: String) {
    @tailrec
    def check(str: String, prev: Char, head: Char, accumulator: List[Char]): List[Char] =
      str match {
        case ""=> if (prev == head) prev :: accumulator else accumulator
        case w => check(str.tail, str.head, head,
          if (prev == str.head) prev :: accumulator else accumulator)
      }


    val duplicates = check(data.tail, data.head, data.head, Nil)
    val digitSum =
      duplicates.map((c: Char) => c.asDigit).foldLeft(0)((m, n) => m + n)
    println(digitSum)
  }

  def main(args: Array[String]) {
    val data = scala.io.Source.stdin.getLines.mkString
    run(data)
  }
}
