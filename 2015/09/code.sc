object TravellingSanta {
  private val pathRegex = raw"""(\w+) to (\w+) = (\d+)""".r
  def parsePaths(str: String): List[((String, String), Int)] = {
    val pathRegex(city1, city2, dist) = str
    List(((city1, city2) -> dist.toInt), ((city2, city1) -> dist.toInt))
  }

  def pathDistances(cities: List[String], paths: Map[(String, String), Int]): List[Int] = {
    cities.sliding(2)
      .map(pair =>
        paths((pair(0), pair(1)))
      ).toList
  }

  def bestPath(cities: List[String], paths: Map[(String, String), Int]): List[String] = {
    cities.permutations.minBy(pathDistances(_, paths).sum)
  }

  def worstPath(cities: List[String], paths: Map[(String, String), Int]): List[String] = {
   cities.permutations.maxBy(pathDistances(_, paths).sum)
  }

  def run(cityDistances: Iterator[String]) {
    val paths = cityDistances.flatMap(parsePaths).toMap
    val cities = paths.keys
      .flatMap(pair => List(pair._1, pair._2))
      .toSet
      .toList

    val santaPath = bestPath(cities, paths)
    println(pathDistances(santaPath, paths).sum)
    val santaPath2 = worstPath(cities, paths)
    println(pathDistances(santaPath2, paths).sum)
  }

  def main(args: Array[String]) {
    val data = io.Source.stdin.getLines

    run(data)
  }
}
