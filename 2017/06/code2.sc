object Reallocator {
  def maxIndex(list: List[Int]): (Int, Int) = {
    list.zipWithIndex.maxBy(_._1)
  }

  def cycle[A](list: List[A]): Iterator[A] = {
    Iterator.continually(list.toStream).flatten
  }

  def updateState(state: List[Int]): List[Int] = {
    val (max, ind) = maxIndex(state)
    val quotient = max / state.length
    val remain = max % state.length
    val stateChange = cycle((1 to remain).map(_ => quotient + 1).toList
      ++ (1 to (state.length - remain)).map(_ => quotient))
      .drop((state.length - (ind + 1)) % state.length)
      .take(state.length)
      .toList


    val newState = state.updated(ind, 0).zip(stateChange).map({
      case (a, b) => a + b
    })

    newState
  }

  def run(banks: List[Int]) {
    val (_, _, sequence) = Iterator.from(0)
      .scanLeft((banks, Set[List[Int]](banks), List[List[Int]](banks)))({
      case ((state, seenStates, allStates), _) =>
        val newState = updateState(state)

        (newState, seenStates + newState, allStates :+ newState)
      }).dropWhile({case (_, s, l) => s.size == l.length}).take(1).toList.head

    val iters = (sequence.length - 1) - sequence.indexOf(sequence.last)
    println(iters)
  }

  def main(args: Array[String]) {
    val data = scala.io.Source.stdin.getLines.mkString.trim

    val intData = data.split("\\s").iterator.map((s) => s.toInt).toList

    run(List(0, 2, 7, 0))
    run(intData)
    
  }
}
