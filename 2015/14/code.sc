case class Reindeer(name: String, speed: Int, duration: Int, sleep: Int) {
  def travelledAt(t: Int): Int = {
    val rounds = t / (duration + sleep)
    val remain = t % (duration + sleep)
    rounds * duration * speed + (if (remain >= duration) duration * speed
                                 else remain * speed)
  }
}

object Reindeers {
  private val reindeerRegex = raw"""(\w+) (?:\w+ ){2}(\d+) (?:.+ ){2}(\d+) (?:.+ ){6}(\d+).*""".r
  def parseReindeer(reindeerStr: String): Reindeer = {
    val reindeerRegex(name, speed, duration, sleep) = reindeerStr
    Reindeer(name, speed.toInt, duration.toInt, sleep.toInt)
  }

  def run(reindeerStrings: Iterator[String]) {
    val reindeers = reindeerStrings.map(parseReindeer).toList
    
    val furthest2503 = reindeers.maxBy(r => r.travelledAt(2503))
    println(furthest2503.travelledAt(2503))
  }

  def main(args: Array[String]) {
    val data = io.Source.stdin.getLines

    run(data)
  }
}
