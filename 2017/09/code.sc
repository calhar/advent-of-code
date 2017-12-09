object Groups {
  def garbage(seq: Iterator[Char]) {
    seq.map({c => if (c == '!') {seq.next; c} else c}).find(_ == '>')
  }

  def group(seq: Iterator[Char]): Int = {
    val (score, _) = seq.foldLeft((0, 0))({case ((s, l), c) => {
      c match {
        case '{' => (s + l + 1, l + 1)
        case '<' => {garbage(seq); (s, l)}
        case '}' => (s, l - 1)
        case c => (s, l)
      }
    }})
    score
  }

  def run(str: String) {
    println(group(str.toIterator))
  }

  def main(args: Array[String]) {
    val data = scala.io.Source.stdin.getLines.next

    run(data)
  } 
}
