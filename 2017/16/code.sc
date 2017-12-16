object Dance {
  def dance(moves: List[String], initPositions: Vector[Char]):
      Vector[Char] = {
    moves.foldLeft(initPositions)(
      (positions, move) => move.head match {
        case 's' => {
          val x = move.tail.toInt
          positions.drop(positions.length - x) ++
            positions.take(positions.length - x)
        }
        case 'x' => {
          val Array(idA, idB) = move.tail.split("/").map(id => id.toInt)
          val (valA, valB) = (positions(idA), positions(idB))
          positions.updated(idA, valB).updated(idB, valA)
        }
        case 'p' => {
          val Array(valA, valB) = move.tail.split("/").map(_.head)
          val (idA, idB) = (positions.indexOf(valA), positions.indexOf(valB))
          positions.updated(idA, valB).updated(idB, valA)
        }
      }
      )
  }

  def findCycle(moves: List[String], initPositions: Vector[Char])
      : (Int, Int, List[Vector[Char]]) = {
    val (_, _, sequence) = Iterator.from(1)
      .scanLeft((initPositions,
                 Set[Vector[Char]](initPositions),
                 List[Vector[Char]](initPositions)))({
        case ((state, seenStates, allStates), _) =>
          val newState = dance(moves, state)

          (newState, seenStates + newState, allStates :+ newState)
        }).dropWhile({case (_, s, l) => s.size == l.length}).take(1).toList.head

    val iters = (sequence.length - 1) - sequence.indexOf(sequence.last)
    (sequence.indexOf(sequence.last), iters, sequence)

  }

  def run(moves: List[String], dancers: Int) {
    val initPositions = (97 until 97 + dancers).map(_.toChar).toVector

    val danced = dance(moves, initPositions)

    val (start, length, cyclePositions) =
      findCycle(moves, initPositions)

    println(danced.mkString)

    val cyclesLeft = (1e9.toInt - start) % length

    println(cyclePositions(cyclesLeft).mkString)
  }
  
  def main(args: Array[String]) {
    val data = scala.io.Source.stdin.getLines.next.split(",").toList

    run(List("s1", "x3/4", "pe/b"), 5)
    run(data, 16)
  }
}
