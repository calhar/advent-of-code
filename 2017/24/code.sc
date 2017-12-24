object Ports {
  type Component = (Int, Int)

  implicit class ComponentHelper(c: Component) {
    def +(t: Component) = (c._1 + t._1, c._2 + t._2)
    def strength: Int = c._1 + c._2
    def connects(p: Int) = c._1 == p || c._2 == p
  }

  def buildBridges(components: Vector[Component])
      : Vector[Vector[Component]] = {
    def extendBridges(bridges: Vector[Vector[Component]],
                      componentsUnusedList: Vector[Vector[Component]],
                      finishedBridges: Vector[Vector[Component]])
        : Vector[Vector[Component]] = {
      bridges match {
        case empty if bridges.isEmpty => finishedBridges
        case bridges => {
          val (bridge, bridgesTail) = (bridges.head, bridges.tail)
          val (unused, unusedTail) = (componentsUnusedList.head, componentsUnusedList.tail)
          val pins = bridge.foldLeft(0)((p, comp) => {
            if (p == comp._1) comp._2 else comp._1
          })
          val validComps = unused.filter(_.connects(pins))
          if (validComps.length == 0) {
            extendBridges(bridgesTail,
                          unusedTail,
                          finishedBridges :+ bridge)
          } else {
            val unusedList = validComps.map(comp =>
                unused.filter(_ != comp)
            )
            val extendedBridges = validComps.map(comp =>
                bridge :+ comp
            )
            extendBridges(extendedBridges ++ bridgesTail,
                          unusedList ++ unusedTail,
                          finishedBridges)
          }
        }
      }
    }
    val validStarts = components.filter(_.connects(0))
    val remainingList = validStarts.map(start =>
        components.filter(_ != start)
    )
    val initBridges = validStarts.map(Vector(_))
    extendBridges(initBridges, remainingList, Vector())
  }

  def run(componentData: Iterator[String]) {
    val components = componentData.map(compStr => {
      val split = compStr.split("/")
      (split(0).toInt, split(1).toInt)
    }).toVector

    val bridges = buildBridges(components)
    println(bridges.map(bridge => bridge.map(_.strength).sum).max)

    val longest = bridges.map(bridge => bridge.length).max
    val longestStrongest = bridges.filter(bridge => bridge.length == longest)
      .map(bridge => bridge.map(_.strength).sum).max
    println(longestStrongest)
  }

  def main(args: Array[String]) {
    val data = io.Source.stdin.getLines

    run(data)
  }
}
