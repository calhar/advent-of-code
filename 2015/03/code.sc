object Santa {
  implicit class TupeHelper(t: (Int, Int)) {
    def +(c: (Int, Int)) = (c._1 + t._1, c._2 + t._2)
  }

  case class Santa(loc: (Int, Int), 
                   presents: Map[(Int, Int), Int] = Map(((0,0),1)).withDefaultValue(0)) {
    def getDir(c: Char): Santa = {
      val dir = c match {
        case '^' => (-1, 0)
        case 'v' => (1, 0)
        case '<' => (0, -1)
        case '>' => (0, 1)
      }
      val newLoc = loc + dir
      Santa(newLoc, presents.updated(newLoc, presents(newLoc) + 1))
    }
  }

  def run(route: String) {
    val santa = route.foldLeft(
      Santa((0, 0), Map(((0, 0), 1)).withDefaultValue(0)))(
        (santa, dir) => santa.getDir(dir)      
      )
    println(santa.presents.size)

    val (realSanta, roboSanta) = route.sliding(2,2).foldLeft(
      (Santa((0, 0)), Santa((0, 0))))(
        (santas, dirs) => (santas._1.getDir(dirs(0)), santas._2.getDir(dirs(1)))
      )
    val allPresents = realSanta.presents ++
      roboSanta.presents.map {case (k, v) => 
        (k, v + realSanta.presents.getOrElse(k,0))
      }

    println(allPresents.size)
  }

  def main(args: Array[String]) {
    val data = io.Source.stdin.getLines.next

    run(data)
  }
}
