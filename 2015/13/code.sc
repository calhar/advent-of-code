object Dinner {
  private val relationRegex = raw"""(\w+) would (\w+) (\d+) (?:\w+ ){6}(\w+).""".r
  def parseRelation(relation: String): ((String, String), Int) = {
    val relationRegex(person1, modify, amount, person2) = relation
    ((person1, person2), (modify match {case "gain" => 1 case "lose" => -1}) * amount.toInt)
  }

  def run(relationStrings: Iterator[String]) {
    val relationsMap = relationStrings.map(parseRelation).toMap
    val guests = relationsMap.keys.flatMap(pair => List(pair._1, pair._2))

    val bestArrange = guests.toList.tail
    .permutations
    .map(l => l :+ guests.head)
    .map(l => 
      (l :+ l.head).sliding(2)
      .map(pair =>
        relationsMap((pair(0), pair(1))) + relationsMap((pair(1), pair(0)))
      ).sum
    ).max

    println(bestArrange)
  }

  def main(args: Array[String]) {
    val data = io.Source.stdin.getLines

    run(data)
  }
}
