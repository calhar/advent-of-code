object Towers {
  def run(lines: List[String]) {
    val progNames = lines.map( line => (line.split(" "))(0) ).toSet
    val linkedNames = lines.map(
        line => {
          line.split("->")
        }
      ).filter( _.length == 2 ).map(
        pair => {
          val linked = pair(1).split(", ").map(_.trim)
          linked
        }
      ).flatten.toSet

    println((progNames &~ linkedNames).head)
  }

  def main(args: Array[String]) {
    val data = scala.io.Source.stdin.getLines.toList
 
    run(data)
  }
}
