object Elves {
  def factors(n: Int): List[Int] = {
    Iterator.from(1)
      .takeWhile(i => i * i <= n)
      .flatMap(i => if (n % i == 0) List(i, n / i) else None)
      .toList
  }

  def calcPresents(houseNo: Int): Int = {
    factors(houseNo)
      .map(e => e * 10)
      .sum
  }

  def limitedPresents(houseNo: Int, elfLimit: Int): Int = {
    factors(houseNo)
      .filter(e => e * elfLimit >= houseNo)
      .map(e => e * 11)
      .sum
  }

  def run(presents: Int) {
    val house = Iterator.from(1).find(calcPresents(_) > presents).get
    println(house)

    val house2 = Iterator.from(1).find(limitedPresents(_, 50) > presents).get
    println(house2)
  }

  def main(args: Array[String]) {
    val data = io.Source.stdin.getLines.next.toInt

    run(data)
  }
}
