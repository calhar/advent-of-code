object Pipes {
  def root(unions: Vector[Int], id: Int): Int = {
    var parent = unions(id)
    while (parent != unions(parent)) {
      parent = unions(parent)
    }
    parent
  }

  def union(unions: Vector[Int], l: Int, r: Int): Vector[Int] = {
    val rootL = root(unions, l)
    val rootR = root(unions, r)
    if (rootL == rootR) unions

    val newRoot = math.min(rootL, rootR)
    unions.updated(rootL, newRoot).updated(rootR, newRoot)
  }

  def run(links: List[String]) {
    val unions = links.map(l => l.split(" ").toList)
      .foldLeft((0 until links.length).toVector)((unions, line) => {
        val prog = line.head.toInt
        val linked = line.drop(2).map(l => l.replace(",", "").toInt)
        linked.foldLeft(unions)((u, l) => union(u, prog, l))
      })


    println(unions)
    val roots = (0 until links.length).map(id => root(unions, id))
    println(roots.count(_ == 0))
    println(roots.toSet.size)
  }

  def main(args: Array[String]) {
    val data = scala.io.Source.stdin.getLines.toList

    run(data)
  }
}
