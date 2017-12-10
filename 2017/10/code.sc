object Knots {
  def cycle[A](list: List[A]): Iterator[A] = {
    Iterator.continually(list.toStream).flatten
  }

  def run(lengths: List[Int]) {
    val (cycled, skip) = lengths.foldLeft(((0 until 256).toList, 0))({
        case ((sequence, skip), length) => {
          val switch = sequence.take(length).reverse ++ sequence.drop(length)
          val shift = cycle(switch)
            .drop(skip + length).take(sequence.length).toList
          (shift, skip + 1)
        }
      })

    val skipped = (lengths.sum + skip * (skip - 1) / 2) % cycled.length
    val trueCycle = cycle(cycled).drop(cycled.length - skipped)
        .take(cycled.length).toList

    println(trueCycle.take(2).product)
  }

  def main(args: Array[String]) {
    val data = scala.io.Source.stdin.getLines.next
        .split(',').map(_.toInt).toList

    run(data)
  }
}
