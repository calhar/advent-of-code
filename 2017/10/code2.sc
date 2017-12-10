object Knots {
  def cycle[A](list: List[A]): Iterator[A] = {
    Iterator.continually(list.toStream).flatten
  }

  def genPattern(bytes: String): List[Int] = {
    bytes.map(_.toInt).toList ++ List(17, 31, 73, 47, 23)
  }

  def run(bytes: String) {
    val lengths = genPattern(bytes)
    val (cycled, skip) = cycle(lengths).take(64 * lengths.length)
      .foldLeft(((0 until 256).toList, 0))({
        case ((sequence, skip), length) => {
          val switch = sequence.take(length).reverse ++ sequence.drop(length)
          val shift = cycle(switch)
            .drop(skip + length).take(sequence.length).toList
          (shift, skip + 1)
        }
      })

    val skipped = (lengths.sum * 64 + skip * (skip - 1) / 2) % cycled.length
    val trueCycle = cycle(cycled).drop(cycled.length - skipped)
      .take(cycled.length).toList
 
    // dense hash
    val hash = trueCycle.sliding(16, 16)         // Split into 16 groups of 16
      .map(v => {                                // For each group
        val h = v.foldLeft(0)(_ ^ _)             // Xor every member together
          .toHexString                           // To Hex
        (0 until 2 - h.length).map(_ => '0')     // Pad to 2 bytes with leading
          .mkString + h                          // zero
      }).mkString

    println(hash)
  }

  def main(args: Array[String]) {
    val data = scala.io.Source.stdin.getLines.next

    run(data)
  }
}
