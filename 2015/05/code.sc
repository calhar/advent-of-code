import scala.util.matching.Regex

object NiceStrings {
  val vowels = raw"(\w*[aeiou]){3,}\w*".r
  val repeats = raw"\w*(\w)\1\w*".r
  val contains = raw"\w*(xy|ab|cd|pq)\w*".r
  val anyTwo = raw"\w*(\w\w)\w*\1\w*".r
  val triplet = raw"\w*(\w)\w\1\w*".r

  implicit class RegexHelper(val regex: Regex) extends AnyVal {
    def matches(s: String) = regex.pattern.matcher(s).matches
  }

  def run(strings: List[String]) {
    var niceStrings = strings.filter(vowels.matches)
      .filter(repeats.matches)
      .filterNot(contains.matches)
    println(niceStrings.length)

    var niceStrings2 = strings.filter(anyTwo.matches)
      .filter(triplet.matches)

    println(niceStrings2.length)
  }

  def main(args: Array[String]) {
    val data = io.Source.stdin.getLines.toList

    run(data)
  }
}
