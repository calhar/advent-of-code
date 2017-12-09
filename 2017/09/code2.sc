object Groups {
  def garbage(seq: Iterator[Char]): Int = {
    seq.map({c => if (c == '!') {seq.next; c} else c})
      .filterNot(_=='!').indexOf('>')
  }

  def group(seq: Iterator[Char]): Int = {
    val removed = seq.foldLeft(0)((r, c) =>{
      c match {
        case '<' => r + garbage(seq)
        case c => r 
      }
    })
    removed
  }

  def run(str: String) {
    println(group(str.toIterator))
  }

  def main(args: Array[String]) {
    val data = scala.io.Source.stdin.getLines.next

    run(data)
  } 
}
