object Spinlock {
  def cycle[A](list: List[A]): Iterator[A] = {
    Iterator.continually(list.toStream).flatten
  }

  def run(step: Int) {
    val spinBuffer = (1 until 2018).foldLeft(List[Int](0))(
      (buffer, input) => {
        cycle(buffer).drop(step).take(buffer.length).toList :+ input
      }
      )

    println(spinBuffer.head)

    val out = (1 to 5e7.toInt).foldLeft((0, 0))({
      case ((afterZero, pos), value) => {
        val updatePos = ((pos + step) % value) + 1
        val newZero = if (updatePos == 1) value else afterZero
        (newZero, updatePos)
      }
    }
    )
    println(out)
  }

  def main(args: Array[String]) {
    val data = scala.io.Source.stdin.getLines.next.toInt

    run(3)
    run(data)
  }
}
