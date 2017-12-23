object Prime extends App {
  def prime(p: Int): Boolean = {
    !(2 to math.sqrt(p).toInt).map(p % _ == 0).exists(b => b)
  }
  val primes = (107900 to 124900 by 17).map(!prime(_)).count(b => b)
  println(primes)
}
