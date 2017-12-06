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
    val stateChange = cycle(List.tabulate(remain)(_ => quotient + 1)
      ++ List.tabulate(state.length - remain)(_ => quotient))
      .drop((state.length - (ind + 1)) % state.length)
      .take(state.length)
      .toList


    val newState = state.updated(ind, 0).zip(stateChange).map({
      case (a, b) => a + b
    })

    newState
  }

  def run(banks: List[Int]) {
    val iters = Iterator.from(1).scanLeft((banks, Set[List[Int]](banks), 0))({
      case ((state, seenStates, _), count) =>
        val newState = updateState(state)

        (newState, seenStates + newState, count)
      }).dropWhile({case (_, s, c) => s.size > c}).take(1).toList.head._3
    println(iters)
  }

  def main(args: Array[String]) {
    val data = scala.io.Source.stdin.getLines.mkString.trim

    val intData = data.split("\\s").iterator.map((s) => s.toInt).toList

    run(List(0, 2, 7, 0))
    run(intData)
    
  }
}
