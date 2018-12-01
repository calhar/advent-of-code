object Frequency {
  def cycle[A](list: List[A]): Iterator[A] = {
    Iterator.continually(list.toStream).flatten
  }

  def run(data: List[String]) {
    val frequencies = data.map((s: String) => s.toInt)

    val duplicateFreq = cycle(frequencies).scanLeft(Set(0), 0, false) ({ (seen, frq) =>
      val newFreq = seen._2 + frq
      val contained = seen._1(newFreq)
      (seen._1 + newFreq, newFreq, contained)
    }).find(tuple=> tuple._3).get._2
    println(duplicateFreq)
  }

  def main(args: Array[String]) {
    val data = scala.io.Source.stdin.getLines.toList
    run(data)
  }
}
