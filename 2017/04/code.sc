object Passphrase {
  def valid(phrases: List[String]) = {
    def recurValid(phrases: List[String], acc: List[Boolean]): List[Boolean] =
      phrases match {
        case Nil => acc
        case x::xs => recurValid(xs, !(xs.contains(x)) :: acc)
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

    run(data)
  }
}
