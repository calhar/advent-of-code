import scala.annotation.tailrec

object Passphrase {
  def run(passphrases: List[String]) {
    val validPassCount = (for (passphrase <- passphrases) yield {
      val phrases = passphrase.split(" ").toList.map((x) => x.sorted)
      phrases.combinations(2).map((x) => x(0) != x(1)).reduceLeft(_ && _)
    }).filter(_ == true).size

    println(validPassCount) 

  }

  def main(args: Array[String]) {
    val data = scala.io.Source.stdin.getLines.toList
    run(data)
  }
}
