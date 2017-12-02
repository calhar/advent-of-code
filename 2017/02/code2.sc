import scala.annotation.tailrec

object Checksum {
  def divisibleTuple(vals: List[Int]): Tuple2[Int, Int] = {
    @tailrec
    def divisOuter(list: List[Int], acc: List[Option[Tuple2[Int, Int]]]):
      List[Option[Tuple2[Int, Int]]] = 
      list match {
        case Nil => acc
        case h :: xs => divisOuter(xs, divisInner(h, xs) :: acc)
      }
    @tailrec
    def divisInner(h: Int, list: List[Int]): Option[Tuple2[Int, Int]] =
      list match {
        case Nil => None
        case x :: xs => if (h % x == 0) Some((h, x))
                        else if (x % h == 0) Some(x, h)
                        else divisInner(h, xs)
      }
    

    val divisible= divisOuter(vals, Nil).flatten
    divisible.head
  }

  def run(data: List[List[Int]]) {
    val checksum = data.map((line) => divisibleTuple(line))
      .map({case (max, min) => max / min})
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
