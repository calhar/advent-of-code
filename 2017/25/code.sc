import scala.collection.mutable.Map

case class State(transitions: Vector[(Int, Int, Int)]) {
  def apply(value: Int): (Int, Int, Int) = {
    transitions(value)
  }
}

class Machine(startState: Int,
              tape: Map[Int, Int],
              states: Vector[State]){
  var cursor = 0
  var state = states(startState)

  def step(): Machine = {
    val curVal = tape(cursor)
    val (writeVal, cursorChange, nextState) = state(curVal)
    state = states(nextState)
    tape(cursor) = writeVal
    cursor += cursorChange
    this
  }

  def seeTape(): collection.Map[Int, Int] = tape.toMap
}

object Machine {
  def apply(startState: Int, tape: Map[Int, Int], states: Vector[State])
      : Machine = {
    new Machine(startState, tape, states)
  }
}

object Turing {
  def parseHeader(header: String): (Int, Int) = {
    val startStateId = header.charAt(15) - 'A'
    val steps = (header.split(" "))(8).toInt
    (startStateId, steps)
  }

  def parseState(stateStr: String): State = {
    val transitions = stateStr.split("\n").toIterator
      .drop(1).grouped(4).map(transitionStr => {
        ((transitionStr(0).charAt(26) - '0').toInt,
         ((transitionStr(1).charAt(22) - '0').toInt,
           transitionStr(2).split(" ")(10) match {
             case "right." => 1
             case "left." => -1
           },
           (transitionStr(3).charAt(26) - 'A').toInt))
      }).toVector.sortBy(_._1).unzip
    State(transitions._2)
  }

  def run(blueprint: Iterator[String]) {
    val (initStateId, steps) = parseHeader(blueprint.next)
    val states = blueprint.map(state => parseState(state)).toVector

    val initMachine = 
      Machine(initStateId, Map[Int, Int]().withDefaultValue(0), states)

    val finalState =
      Iterator.iterate(initMachine)(machine => machine.step).drop(steps).next

    println(finalState.seeTape.valuesIterator.count(_ == 1))
  }

  def main(args: Array[String]) {
    val data = io.Source.stdin.mkString.split("\n\n").toIterator

    run(data)
  }  
}
