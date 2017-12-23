import scala.collection.mutable.ArrayBuffer

case class Process(instrPnt: Int,
                   waiting: Boolean,
                   sent: Int,
                   inQueue: ArrayBuffer[Long],
                   outQueue: ArrayBuffer[Long],
                   registers: Map[String, Long]) {
  def send(x: String): Process = {
    Process(instrPnt + 1,
            false,
            sent + 1,
            inQueue,
            outQueue += registers(x),
            registers)
  }

  def store(x: String, y: String): Process = {
    Process(instrPnt + 1,
            false,
            sent,
            inQueue,
            outQueue,
            registers + (x -> registers(y)))
  }

  def add(x: String, y: String): Process = {
    Process(instrPnt + 1,
            false,
            sent,
            inQueue,
            outQueue,
            registers + (x -> (registers(x) + registers(y))))
  }

  def mul(x: String, y: String): Process = {
    Process(instrPnt + 1,
            false,
            sent,
            inQueue,
            outQueue,
            registers + (x -> (registers(x) * registers(y))))
  }

  def mod(x: String, y: String): Process = {
    Process(instrPnt + 1,
            false,
            sent,
            inQueue,
            outQueue,
            registers + (x -> (registers(x) % registers(y))))
  }

  def jump(x: String, y: String): Process = {
    Process((if (registers(x) > 0) instrPnt + registers(y).toInt
             else instrPnt + 1),
            false,
            sent,
            inQueue,
            outQueue,
            registers)
  }

  def receive(x: String): Process = {
    if (inQueue.length == 0) Process(instrPnt,
                                     true,
                                     sent,
                                     inQueue,
                                     outQueue,
                                     registers)
    else Process(instrPnt + 1,
                 false,
                 sent,
                 inQueue,
                 outQueue,
                 registers + (x -> inQueue.remove(0)))
  }
}
  
sealed trait Instruction {
  def apply(process: Process): Process
}

case class Send(x: String) extends Instruction {
  def apply(process: Process) = process.send(x)
}

case class Store(x: String, y: String) extends Instruction {
  def apply(process: Process) = process.store(x, y)
}

case class Add(x: String, y: String) extends Instruction {
  def apply(process: Process) = process.add(x, y)
}

case class Mul(x: String, y: String) extends Instruction {
  def apply(process: Process) = process.mul(x, y)
}

case class Mod(x: String, y: String) extends Instruction {
  def apply(process: Process) = process.mod(x, y)
}

case class Receive(x: String) extends Instruction {
  def apply(process: Process) = process.receive(x)
}

case class Jump(x: String, y: String) extends Instruction {
  def apply(process: Process) = process.jump(x, y)
}

object Duet {
  def run(program: List[String]) {
    val instructions: Vector[Instruction] = program.map(instruction => {
      val operands = instruction.split(" ")
      operands.head match {
        case "snd" => Send(operands(1))
        case "set" => Store(operands(1), operands(2))
        case "add" => Add(operands(1), operands(2))
        case "mul" => Mul(operands(1), operands(2))
        case "mod" => Mod(operands(1), operands(2))
        case "rcv" => Receive(operands(1))
        case "jgz" => Jump(operands(1), operands(2))
      }
    }
    ).toVector

    val (aQueue, bQueue) = (ArrayBuffer[Long](), ArrayBuffer[Long]())
    val initMap = ('a' to 'z').map(c => c.toString -> 0L)
                  .toMap
                  .withDefault(str => str.toLong)
    val initProcessA = Process(0,
                               false,
                               0,
                               bQueue,
                               aQueue,
                               initMap + ("p" -> 0))
    val initProcessB = Process(0,
                               false,
                               0,
                               aQueue,
                               bQueue,
                               initMap + ("p" -> 1))
    val (stateA, stateB) =
      Iterator.iterate((initProcessA, initProcessB))(
        {
          case (processA, processB) => {
            val updateA = instructions(processA.instrPnt)(processA)
            val updateB = instructions(processB.instrPnt)(processB)
            (updateA, updateB)
          }
        }
      ).find(states => states._1.waiting && states._2.waiting).get
    println(stateB.sent)
  }

  def main(args: Array[String]) {
    val data = scala.io.Source.stdin.getLines.toList

    run(data)
  }
}
