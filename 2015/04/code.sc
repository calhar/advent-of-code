object AdventCoins {
  lazy val md5er = java.security.MessageDigest.getInstance("MD5")
  def md5Hex(in: String): String = {
    md5er.digest(in.getBytes).map(_.formatted("%02x")).mkString
  }

  def run(key: String) {
    val fiveZeros = Iterator.from(1)
      .find(i => md5Hex(key + i).take(5) == "00000").get
    println(fiveZeros)

    val sixZeros = Iterator.from(1)
      .find(i => md5Hex(key + i).take(6) == "000000").get
    println(sixZeros)
  }
  def main(args: Array[String]) {
    val data = io.Source.stdin.getLines.next

    run(data)
  }
}
