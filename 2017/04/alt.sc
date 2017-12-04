import scala.annotation.tailrec

object Passphrase {
  def run(passphrases: List[String]) {
    val validPassCount = (for (passphrase <- passphrases) yield {
      val phrases = passphrase.split(" ").toList
      phrases.combinations(2).map((x) => x(0) != x(1)).reduceLeft(_ && _)
    }).filter(_ == true).size

   val validPassCount2 = passphrases.count((passphrase) => {
     val phrases = passphrase.split(" ").toList
     phrases.distinct == phrases
   })

    println(validPassCount) 
    println(validPassCount2)

  }

  def main(args: Array[String]) {
    val data = scala.io.Source.stdin.getLines.toList

    run(data)
  }
}
