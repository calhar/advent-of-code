object KnotHash {
  private def cycle[A](list: List[A]): Iterator[A] = {
    Iterator.continually(list.toStream).flatten
  }

  private def genPattern(bytes: String): List[Int] = {
    bytes.map(_.toInt).toList ++ List(17, 31, 73, 47, 23)
  }

  private def knot(sequence: List[Int], switchVals: (Int, Int)): List[Int] = {
    val (length, skip) = switchVals
    val switch = sequence.take(length).reverse ++ sequence.drop(length)
    cycle(switch).drop(skip + length).take(sequence.length).toList
  }

  private def sparseHash(seed: String): List[Int] = {
    val lengths = genPattern(seed)
    val knotted = cycle(lengths)
      .zip(Iterator.from(0))
      .take(64 * lengths.length)
      .foldLeft((0 until 256).toList)(knot)

    val skip = lengths.length * 64
    val skipped = (lengths.sum * 64 + skip * (skip - 1) / 2) % knotted.length
    cycle(knotted).drop(knotted.length - skipped)
      .take(knotted.length).toList

  }

  def hash(seed: String): String = {
    val sparse = sparseHash(seed)
    // dense hash
    sparse.grouped(16)                           // Split into 16 groups of 16
      .map(v => {                                // For each group
        v.reduce(_ ^ _)                          // Xor every member together
          .formatted("%02x")
      }).mkString
  }
}

case class UnionNode(loc: (Int, Int), parent: Int)

object Defrag {
  def root(unions: Vector[UnionNode], id: Int): Int = {
    var parent = unions(id).parent
    while (parent != unions(parent).parent) {
      parent = unions(parent).parent
    }
    parent
  }

  def union(unions: Vector[UnionNode], l: Int, r: Int): Vector[UnionNode] = {
    val rootL = root(unions, l)
    val rootR = root(unions, r)
    if (rootL == rootR) unions

    val newRoot = math.min(rootL, rootR)
    unions.updated(rootL, unions(rootL).copy(parent = newRoot))
      .updated(rootR, unions(rootR).copy(parent = newRoot))
  }

  def unionNeighbours(locToIds: Map[(Int, Int), Int])
                     (unions: Vector[UnionNode],
                      loc: (Int, Int)): Vector[UnionNode] = {
    val neighbours = List((-1, 0), (0, -1), (0, 1), (1, 0))
    val (x, y) = loc
    neighbours.filter({case (a, b) => locToIds.contains((x + a, y + b))})
      .foldLeft(unions)({case (u, (a, b)) =>
        union(u, locToIds(loc), locToIds((x + a, y + b)))
    })
  }

  def locationMapAndUnionData(locations: List[(Int, Int)]):
      (Map[(Int, Int), Int], Vector[UnionNode]) = {
    val nodeIds = Iterator.from(0)
    locations.foldLeft((Map[(Int, Int), Int](), Vector[UnionNode]()))({
      case ((locToIds, unions), loc) => {
        val id = nodeIds.next
        (locToIds + (loc -> id), unions :+ UnionNode(loc, id))
      }
    })
  }

  def run(seed: String) {
    val hashes = (0 until 128).map(seed + "-" + _).map(s =>
        KnotHash.hash(s))
    val binaryHashes = hashes.map(hash =>
        hash.grouped(4).map(Integer.parseInt(_, 16))
          .map(word16 => (15 to 0 by -1).map(shift => (word16 >> shift) & 1))
          .flatten.toList
      )

    println(binaryHashes.map(_.count(_ == 1)).sum)

    val fragLocations =
      (for ((hash, idx) <- binaryHashes.zipWithIndex;
           (b, idy) <- hash.zipWithIndex if b == 1)
        yield (idx, idy)
      ).toList

    val (locToIds, unions) = locationMapAndUnionData(fragLocations)

    val unionFind = fragLocations.foldLeft(unions)(unionNeighbours(locToIds))
    val roots = (0 until unionFind.length).map(id => root(unionFind, id))

    println(roots.toSet.size)
  }

  def main(args: Array[String]) {
    val seed = scala.io.Source.stdin.getLines.next
    run("flqrgnkx")
    run(seed)
  }
}
