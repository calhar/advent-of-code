sealed trait Instruction {
  def apply(instr: Int, recovered: Boolean, played: Long, registers: Map[String, Long]): (Int, Boolean, Long, Map[String, Long]) 
}

case class Play(x: String) extends Instruction {
  def apply(instr: Int,
            recovered: Boolean,
            played: Long,
            registers: Map[String, Long]) =
    (instr + 1, false, registers(x), registers)
}

case class Store(x: String, y: String) extends Instruction {
  def apply(instr: Int,
            recovered: Boolean,
            played: Long,
            registers: Map[String, Long]) =
    (instr + 1, false, played, registers + (x -> registers(y)))
}

case class Add(x: String, y: String) extends Instruction {
  def apply(instr: Int,
            recovered: Boolean,
            played: Long,
            registers: Map[String, Long]) =
    (instr + 1, false, played, registers + (x -> (registers(x) + registers(y))))
}

case class Mul(x: String, y: String) extends Instruction {
  def apply(instr: Int,
            recovered: Boolean,
            played: Long,
            registers: Map[String, Long]) =
    (instr + 1, false, played, registers + (x -> registers(x) * registers(y)))
}

case class Mod(x: String, y: String) extends Instruction {
  def apply(instr: Int,
            recovered: Boolean,
            played: Long,
            registers: Map[String, Long]) =
    (instr + 1, false, played, registers + (x -> registers(x) % registers(y)))
}

case class Recover(x: String) extends Instruction {
  def apply(instr: Int,
            recovered: Boolean,
            played: Long,
            registers: Map[String, Long]) =
    (instr + 1, registers(x) > 0, played, registers)
}

case class Jump(x: String, y: String) extends Instruction {
  def apply(instr: Int,
            recovered: Boolean,
            played: Long,
            registers: Map[String, Long]) =
    (if (registers(x) > 0) instr + registers(y).toInt else instr + 1,
     false, played, registers)
}

object Soundcard {
  def run(program: List[String]) {
    val instructions: Vector[Instruction] = program.map(instruction => {
      val operands = instruction.split(" ")
      operands.head match {
        case "snd" => Play(operands(1))
        case "set" => Store(operands(1), operands(2))
        case "add" => Add(operands(1), operands(2))
        case "mul" => Mul(operands(1), operands(2))
        case "mod" => Mod(operands(1), operands(2))
        case "rcv" => Recover(operands(1))
        case "jgz" => Jump(operands(1), operands(2))
      }
    }
    ).toVector

    val first =
      Stream.iterate(
        (0, false, 0L, ('a' to 'z').map(c => c.toString -> 0L).toMap.withDefault(str => str.toLong)))({
          case (instr, _, played, registers) => {
            instructions(instr)(instr, false, played, registers)
          }
        }).find(_._2 == true).get
    println(first)
  }

  def main(args: Array[String]) {
    val data = scala.io.Source.stdin.getLines.toList

    run(data)
  }
}
