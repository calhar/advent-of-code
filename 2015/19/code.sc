object Fission {
  def elements(molecule: String): List[String] =  {
    val caps = "[A-Z]".r
    (caps.findAllIn(molecule)
        .matchData
        .map(_.start)
        .toList :+ molecule.length)
        .sliding(2)
        .map({ sub => molecule.substring(sub(0), sub(1)) })
        .toList
  }

  def run(transformList: List[String], molecule: String) {
    // Create map of transforms (X => Y)
    val transforms = transformList
      .foldLeft(Map[String, List[String]]().withDefaultValue(Nil)) { 
        case (transforms, transform) =>
          val pair = transform.split("=>").map(_.trim)
          val vals = transforms(pair(0))
          transforms + (pair(0) -> (pair(1) :: vals))
      }

    // Get list of elements in order
    val elementList = elements(molecule)

    // Preceeding stores the string of elements before the current one
    // combos is the set of modified molecules we've made by switching elements
    //
    // There's a more intuitive way to do this using indexing to the elementList
    // and creating the entire modified molecule at once, like
    // val modified = poss.filter(_ != el(index))
    //     .map((elementList.take(index).mkString + _ + elementList.drop(index+1))).toSet
    // 
    // But just doing it the way I'd do it in Python, C++
    val mutates = elementList
      .foldLeft(("", Set[String]()))({
        case ((preceeding, combos), el) => {
          val update = combos.map(_ + el)
          val poss = (transforms(el) match {
            case Nil => List(el)
            case list => list
          })

          val updatedPoss = poss.filter(_ != el).map(preceeding + _)
          (preceeding + el, (update.toList ++ updatedPoss).toSet)
        }
      })

    println(mutates._2.size)
  }

  def main(args: Array[String]) {
    val data = scala.io.Source.stdin.getLines.toList

    val transforms = data.init.init
    val molecule = data.last
 
    run(List("H => HO", "H => OH", "O => HH"), "HOH")
    run(transforms, molecule)
  }
}
