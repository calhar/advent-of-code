object Groups {
  def garbage(seq: Iterator[Char]) {
    seq.map({c => if (c == '!') {seq.next; c} else c}).find(_ == '>')
  }

  def group(seq: Iterator[Char]): Int = {
    var layer = 0
    val score = seq.foldLeft(0)((s, c) =>{
      c match {
        case '{' => {layer += 1; s + layer}
        case '<' => {garbage(seq); s}
        case '}' => {layer -= 1; s}
        case c => s
      }
    })
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
