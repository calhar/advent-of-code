import scala.annotation.tailrec

object Passphrase {
  def valid(phrases: List[String]) = {
    def containsAnagram(word: String, words: List[String]): Boolean = {
      @tailrec
      def helper(word: String, words: List[String]): Boolean = 
        words match {
          case Nil => false
          case x::xs => (x.sorted == word) || helper(word, xs)
        }

      helper(word.sorted, words)
    }

    @tailrec
    def recurValid(phrases: List[String], acc: List[Boolean]): List[Boolean] = {
      phrases match {
        case Nil => acc
        case x::xs => recurValid(xs, !containsAnagram(x, xs) :: acc)
      }
    }
    recurValid(phrases, Nil).foldLeft(true)(_ && _)
  }

  def run(passphrases: List[String]) {
    val validPassCount = (for (passphrase <- passphrases) yield {
      val phrases = passphrase.split(" ").toList
      valid(phrases)
    }).filter(_ == true).size

    println(validPassCount)

  }

  def main(args: Array[String]) {
    val data = scala.io.Source.stdin.getLines.toList

    run(List("abcde fghij"))
    run(List("abcde xyz ecdab"))
    run(List("a ab abc abd abf abj"))
    run(List("iiii oiii ooii oooi oooo"))
    run(List("oiii ioii iioi iiio"))
    run(data)
  }
}
