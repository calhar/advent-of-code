object Wrapper {
  def run(sizes: Iterator[String]){
    val intRegex = raw"(\d+)".r
    val boxes = sizes.map(s =>
        intRegex.findAllIn(s).toVector.map(_.toInt).sorted
    ).toList

    val paper = boxes.map(b =>
        3 * b(0) * b(1) + 2 * b(0) * b(2) + 2 * b(1) * b(2)
    ).sum
    println(paper)

    val ribbon = boxes.map(b =>
        2 * b(0) + 2 * b(1) + b.product
    ).sum
    println(ribbon)
  }

  def main(args: Array[String]) {
    val data = io.Source.stdin.getLines

    run(data)
  }
}
