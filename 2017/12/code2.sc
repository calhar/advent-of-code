object Pipes {
  def getGroup(id: Int, unions: Map[Int, List[Int]]): Set[Int] = {
    def helper(unions: Map[Int, List[Int]], visited: Set[Int], toVisit: Set[Int]): Set[Int] = {
      toVisit match {
        case empty if toVisit.size == 0 => visited
        case nonempty => {
          val h = toVisit.head
          val next = unions(h).toSet &~ visited
          helper(unions, visited + h, toVisit.tail ++ next)
        }
      }

    }
    helper(unions, Set[Int](id), unions(id).toSet)
  }

  def run(links: List[String]) {
    val unionMap = links.map(l => l.split(" ").toList)
      .foldLeft(Map[Int, List[Int]]())((unions, line) => {
        val prog = line.head.toInt
        val linked = line.drop(2).map(l => l.replace(",", "").toInt)
        unions + (prog -> linked)
      })

    
    println(unionMap)
    val (_, groups) = Iterator.continually(0).scanLeft(((0 until links.length).toSet, 0))({
      case ((remaining, groups), _) => {
        remaining match {
          case empty if remaining.size == 0 => (remaining, groups)
          case nonempty => {
            val group = getGroup(remaining.head, unionMap)
            (remaining &~ group, groups + 1)
          }
        }
      }
      }
      ).find({case (remaining, groups) => remaining == Set[Int]()}).get
    println(groups)
  }

  def main(args: Array[String]) {
    val data = scala.io.Source.stdin.getLines.toList

    run(data)
  }
}
