import scala.annotation.tailrec

object Checksum {
  def divisibleTuple(vals: List[Int]): Tuple2[Int, Int] = {
    @tailrec
    def divisOuter(list: List[Int], acc: List[Tuple2[Int, Int]]):
      List[Tuple2[Int, Int]] = 
      list match {
        case Nil => acc
        case h :: xs => divisOuter(xs, divisInner(h, xs, List()) ++ acc)
      }
    @tailrec
    def divisInner(h: Int, list: List[Int], acc: List[Tuple2[Int, Int]]):
      List[Tuple2[Int, Int]] =
      list match {
        case Nil => acc
        case x :: xs => divisInner(h, xs, 
            (if (h % x == 0) List((h, x))
             else if (x % h == 0) List((x, h))
             else Nil) ++ acc)
      }
    

    val divisible= divisOuter(vals, Nil)
    for (p <- divisible) {println(p)}
    divisible.head
  }

  def run(data: List[List[Int]]) {
    val checksum = data.map((line) => divisibleTuple(line))
      .map({case (max, min) => max / min})
      .foldLeft(0)((acc, n) => acc + n)
    println(checksum)    
  }

  def main(args: Array[String]) {
    //val data = scala.io.Source.stdin.getLines
    val data = Iterator("2 8 9 3", "9 4 7 3", "3 8 6 5")

    val intData = data.map((line) =>
        line.split("\\s").iterator.toList.map((s) => s.toInt)).toList

    run(intData)
  }
}
