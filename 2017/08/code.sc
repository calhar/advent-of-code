object Registers {
  def update(registers: Map[String, Int], tokens: List[String])
      : Map[String, Int] = {
    val reg1 = tokens(0)
    val op = tokens(1)
    val adjust = tokens(2).toInt
    val reg2 = tokens(4)
    val cmpOp = tokens(5)
    val cmpVal = tokens(6).toInt
    val curVal = registers(reg1)
    val cmpReg = registers(reg2)

    val increment = op match {
      case "dec" => -adjust
      case "inc" => adjust
    }

    val compared = cmpOp match {
      case "<" => cmpReg < cmpVal
      case ">" => cmpReg > cmpVal
      case ">=" => cmpReg >= cmpVal
      case "<=" => cmpReg <= cmpVal
      case "==" => cmpReg == cmpVal
      case "!=" => cmpReg != cmpVal
    }

    val newVal = if (compared) curVal + increment else curVal

    registers + (reg1 -> newVal)
  }
  def run(instructions: List[String]) {
    val registers = instructions
      .foldLeft(Map[String, Int]().withDefaultValue(0))({
        case (regs, inst) => {
          val tokens = inst.split(" ").toList
          update(regs, tokens)
        }
      })

    println(registers.values.max)
  }

  def main(args: Array[String]) {
    val data = scala.io.Source.stdin.getLines.toList
 
    run(data)
  }
}
