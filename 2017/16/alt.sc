object Dance {
  def initDancers(dancers: Int): Vector[Char] =
    (97 until 97 + dancers).map(_.toChar).toVector

  def danceTransform(moves: List[String], dancers: Int):
      (Vector[Int], Vector[Char]) = {
    val positionsStart = (0 until dancers).toVector
    val dancersStart = initDancers(dancers)
    moves.foldLeft((positionsStart, dancersStart))({
      case ((positions, dancers), move) => move.head match {
        case 's' => {
          val x = move.tail.toInt
          (positions.drop(positions.length - x) ++
            positions.take(positions.length - x), dancers)
        }
        case 'x' => {
          val Array(idA, idB) = move.tail.split("/").map(id => id.toInt)
          val (valA, valB) = (positions(idA), positions(idB))
          (positions.updated(idA, valB).updated(idB, valA), dancers)
        }
        case 'p' => {
          val Array(valA, valB) = move.tail.split("/").map(_.head)
          val (idA, idB) = (dancers.indexOf(valA), dancers.indexOf(valB))
          (positions, dancers.updated(idA, valB).updated(idB, valA))
        }
      }
    })
  }

  def applyTransforms(transforms: (Vector[Int], Vector[Char]),
                      dancers: Vector[Char]): Vector[Char] = {
    val (posTransform, idTransform) = transforms
    val halfTransformed = posTransform.map(idx => dancers(idx))
    initDancers(dancers.length).map(_.toChar).zip(idTransform)
      .foldLeft(halfTransformed)({
        case (transformed, (valA, valB)) => {
          val idA = halfTransformed.indexOf(valA)
          transformed.updated(idA, valB)
        }
      })
  }

  def squareTransforms(posTransform: Vector[Int],
                       idTransform: Vector[Char]):
      (Vector[Int], Vector[Char]) = {
    val pos = posTransform.map(idx => posTransform(idx))
    val ids = initDancers(idTransform.length).zip(idTransform)
      .foldLeft(idTransform)({
        case (transformed, (valA, valB)) => {
          val idA = idTransform.indexOf(valA)
          transformed.updated(idA, valB)
        }
      })
    (pos, ids)
  }

  def run(moves: List[String], dancers: Int) {
    val dancersStart = initDancers(dancers)
    val (posTransform, idTransform) = danceTransform(moves, dancers)
    val danced = applyTransforms((posTransform, idTransform), dancersStart)
    println(danced.mkString)

    val transforms = 1e9.toInt.toBinaryString.reverse.drop(1)
      .scanLeft(posTransform, idTransform)((txs, _) => 
          squareTransforms(txs._1, txs._2)
      )
    val billion = 1e9.toInt.toBinaryString.reverse.zipWithIndex
      .foldLeft(dancersStart)({
        case (positions, (bit, idx)) => {
          if (bit == '1') applyTransforms(transforms(idx), positions)
          else positions
        }
      })
    println(billion.mkString)
  }
  
  def main(args: Array[String]) {
    val data = scala.io.Source.stdin.getLines.next.split(",").toList

    run(List("s1", "x3/4", "pe/b"), 5)
    run(data, 16)
  }
}
