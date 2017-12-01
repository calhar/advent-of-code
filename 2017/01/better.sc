object Captcha {

  def run(data: String) {
    val n = data.length / 2;

    // Find the duplicates first by concatenating the data with itself to
    // get all the circularity of the string we'll ever need.
    // Remove the first n elements of the "circular" string and zip with the
    // original
    //
    // So if data = "123123" and n = 333
    // data + data = "123123123123"
    // drop(3) = "123123123"
    // data.zip = [(1,1), (2,2), (3,3), (1,1), (2,2), (3,3)]
    //
    // Then map a function to that list so that if both elements of a tuple are
    // identical return the character as a digit, else return 0
    val duplicates = data.zip((data + data).drop(n))
      .map({case (a, b) => if (a == b) a.asDigit else 0})
    val digitSum =
      duplicates.foldLeft(0)((m, n) => m + n)
    println(digitSum)
  }

  def main(args: Array[String]) {
    val data = scala.io.Source.stdin.getLines.mkString
    run(data)
  }
}
