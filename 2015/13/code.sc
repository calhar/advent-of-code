object Dinner {
  private val relationRegex = raw"""(\w+) would (\w+) (\d+) (?:\w+ ){6}(\w+).""".r
  def parseRelation(relation: String): ((String, String), Int) = {
    val relationRegex(person1, modify, amount, person2) = relation
    ((person1, person2), (modify match {case "gain" => 1 case "lose" => -1}) * amount.toInt)
  }

  def getSeatingValue(seating: List[String], relations: Map[(String, String), Int]): Int = {
    (seating :+ seating.head).sliding(2)
    .map(pair =>
      relations((pair(0), pair(1))) + relations((pair(1), pair(0)))
    ).sum

  }

  def getBestArrangement(guests: Set[String], relations: Map[(String, String), Int]): List[String] = {
    guests.toList.tail
    .permutations
    .map(l => l :+ guests.head)
    .maxBy(l => 
      getSeatingValue(l, relations)
    )
  }

  def run(relationStrings: Iterator[String]) {
    val relationsMap = relationStrings.map(parseRelation).toMap
    val guests = relationsMap.keys.flatMap(pair => List(pair._1, pair._2)).toSet

    val bestArrange = getBestArrangement(guests, relationsMap)
    println(getSeatingValue(bestArrange, relationsMap))

    val relationsPlus = relationsMap ++
      guests.flatMap(guest => List((guest, "me") -> 0, ("me", guest) -> 0))
        .toMap
    val guestsPlus = guests + "me"
    val bestArrangePlus = getBestArrangement(guestsPlus, relationsPlus)
    println(getSeatingValue(bestArrangePlus, relationsPlus))
  }

  def main(args: Array[String]) {
    val data = io.Source.stdin.getLines

    run(data)
  }
}
