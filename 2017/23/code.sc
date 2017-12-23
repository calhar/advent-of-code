sealed trait Instruction {
  def apply(instr: Int, recovered: Boolean, mults: Int, registers: Map[String, Long]): (Int, Boolean, Int, Map[String, Long]) 
}

case class Store(x: String, y: String) extends Instruction {
  def apply(instr: Int,
            recovered: Boolean,
            mults: Int,
            registers: Map[String, Long]) =
    (instr + 1, false, mults, registers + (x -> registers(y)))
}

case class Sub(x: String, y: String) extends Instruction {
  def apply(instr: Int,
            recovered: Boolean,
            mults: Int,
            registers: Map[String, Long]) =
    (instr + 1, false, mults, registers + (x -> (registers(x) - registers(y))))
}

case class Mul(x: String, y: String) extends Instruction {
  def apply(instr: Int,
            recovered: Boolean,
            mults: Int,
            registers: Map[String, Long]) =
    (instr + 1, false, mults + 1, registers + (x -> registers(x) * registers(y)))
}

case class Jump(x: String, y: String) extends Instruction {
  def apply(instr: Int,
            recovered: Boolean,
            mults: Int,
            registers: Map[String, Long]) =
    (if (registers(x) != 0) instr + registers(y).toInt else instr + 1,
     false, mults, registers)
}

object Soundcard {
  def run(program: List[String]) {
    val instructions: Vector[Instruction] = program.map(instruction => {
      val operands = instruction.split(" ")
      operands.head match {
        case "set" => Store(operands(1), operands(2))
        case "sub" => Sub(operands(1), operands(2))
        case "mul" => Mul(operands(1), operands(2))
        case "jnz" => Jump(operands(1), operands(2))
      }
    }
    ).toVector

    val finished =
      Iterator.iterate(
        (0, false, 0,
         ('a' to 'h').map(c => c.toString -> 0L)
           .toMap.withDefault(str => str.toLong) + ("a" -> 1L)))({
          case (instr, _, mults, registers) => {
            val state = instructions(instr)(instr, false, mults, registers)
            if (state._1 < 0 || state._1 >= instructions.length) {
              state.copy(_2 = true)
            } else state
          }
        }).find(_._2 == true).get
    println(finished)
    println(finished._4("h"))
  }

  def main(args: Array[String]) {
    val data = scala.io.Source.stdin.getLines.toList

    run(data)
  }
}
