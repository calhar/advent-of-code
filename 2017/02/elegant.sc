object Checksum {

  def run(data: List[List[Int]]) {
    /**
     * So the elegant way to solve these two problems is to obtain the 
     * cross product of each number series with itself excluding identical
     * values and perform the appropriate operation
     * 
     *
     * e.g. [5 9 2 8] => [(5,9), (5,2), (5,8), (9,5), (9,2), (9,8), (2,5), etc]
     *
     * In the case of finding the range between max/min, we do a-b for every
     * tuple and then return the max of the sequence. Since max-min will
     * naturally have the highest range. 
     * In the case of finding the values that are divisible we add another
     * restriction to the for comprehension so that we know we're only getting
     * a tuple (a,b) where a is divisible by b. We yield a/b and take the max
     * or head of the sequence.
     **/
    val checksum1 = data.map((line) => 
        (for (a <- line; b <- line if a != b) yield a - b).max)
        .foldLeft(0)((acc, n) => acc + n)
    val checksum2 = data.map((line) =>
        (for (a <- line; b <- line if a != b && a % b == 0) yield a / b).head)
        .foldLeft(0)((acc, n) => acc + n) 
    println(checksum1)
    println(checksum2)
  }

  def main(args: Array[String]) {
    val data = scala.io.Source.stdin.getLines

    val intData = data.map((line) =>
        line.split("\\s").iterator.toList.map((s) => s.toInt)).toList

    run(intData)
  }
}
