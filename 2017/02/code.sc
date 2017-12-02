object Checksum {
  def minMaxTuple(vals: List[Int]): Tuple2[Int,Int] =
    vals.foldLeft((0, Int.MaxValue))((a:Tuple2[Int,Int], b)=>
        (if (b > a._1) b else a._1, if (b < a._2) b else a._2))

  def run(data: List[List[Int]]) {
    val checksum = data.map((line) => minMaxTuple(line))
      .map({case (max, min) => max - min})
      .foldLeft(0)((acc, n) => acc + n)
    println(checksum)    
  }

  def main(args: Array[String]) {
    val data = scala.io.Source.stdin.getLines

    val intData = data.map((line) =>
        line.split("\\s").iterator.toList.map((s) => s.toInt)).toList

    run(intData)
  }
}
