class Node(val id: String, val weight:Int, val kids: List[Node]) {
  val trueWeight: Int = weight + kids.map( _.trueWeight ).sum

}

object Towers {

  def buildTree(node: String, 
    treeMap: Map[String, List[String]],
    values: Map[String, Int]): Node = {
    val children = treeMap(node).map(buildTree(_, treeMap, values))

    new Node(node, values(node), children)
  }

  def fixWrong(node: Node) {
    def findErrNode(node:Node, error:Int): Node = {
      val childWeights = node.kids.map(_.trueWeight)
      childWeights match {
        case childWeights if childWeights.length == 1 => {
          findErrNode(node.kids(0), error)
        }
        case childWeights if childWeights.forall(_ == childWeights.head) => {
          node
        }
        case childWeights => {
          if (error < 0) {
            findErrNode(node.kids(childWeights.indexOf(childWeights.max)), error)
          } else {
            findErrNode(node.kids(childWeights.indexOf(childWeights.min)), error)
          }
        }
      }
    }

    def findErr(node: Node, error:Int): (Node, Int, Boolean) = {
      node.kids.map(_.trueWeight) match {
        case childWeights if childWeights.length == 1 => {
          findErr(node.kids(0), error)
        }
        case childWeights if childWeights.forall(_ == childWeights.head) => {
          (node, 0, false)
        }
        case childWeights if childWeights.length < 2 => {
          error match {
            case 0 => {
              val errors = childWeights.zip(childWeights.reverse).map (w => w._1 - w._2)
              node.kids.zip(errors)
                .map({case (kid, err) => findErr(kid, err)})
                .filter(_._3 == true).head
   
            }
            case err => {
              (node, err, true)
            }
          }
        }
        case childWeights if childWeights.exists(_ != childWeights.head) => {
          val grouped = childWeights.groupBy(x => x)
            .mapValues(_.size)
            .toSeq
          val mode = grouped.filter(_._2 != 1).head._1
          val antiMode = grouped.filter(_._2 == 1).head._1

          (node.kids(childWeights.indexOf(antiMode)), mode - antiMode, true)
        }
      }
    }

    println(node.kids.map(_.trueWeight))
    val (foundErr, error, found) = findErr(node, 0)
    val errNode = findErrNode(foundErr, error)
    println(errNode.id)
    println(errNode.weight)
    println(error)
  }

  def run(lines: List[String]) {
    val (progNames, valueMap) = lines.foldLeft((Set[String](), Map[String, Int]()))({
      case ((names, valMap), line) =>
        val tokens = line.split(" ")
        val name = tokens(0)
        val weight = tokens(1).filterNot(c => c == '(' || c == ')').toInt
        (names + name, valMap + (name -> weight))
      })
    val (linkMap, linkedNames) = lines.map(
        line => {
          line.split("->")
        }
      ).filter( _.length == 2 )
      .foldLeft((Map[String, List[String]]().withDefaultValue(List()),
                List[String]()))({
        case ((towerMap, linked), split) => {
          val tLinked = split(1).split(", ").map(_.trim).toList
          val name = (split(0).split(" "))(0)
          (towerMap + (name -> tLinked), linked ++ tLinked)
        }
        
      })

    val head = (progNames &~ linkedNames.toSet).head
    val tree = buildTree(head, linkMap, valueMap)

    fixWrong(tree)
  }

  def main(args: Array[String]) {
    val data = scala.io.Source.stdin.getLines.toList
 
    run(data)
  }
}
