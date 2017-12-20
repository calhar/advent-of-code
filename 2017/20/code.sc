class Particle(val position: (Long, Long, Long),
               val velocity: (Long, Long, Long),
               val accel: (Long, Long, Long)) {
 implicit class VectorArithmetic(t: (Long, Long, Long)) {
    import Numeric.Implicits._

    def +(p: (Long, Long, Long)) = (t._1 + p._1, t._2 + p._2, t._3 + p._3)
    def -(p: (Long, Long, Long)) = (t._1 - p._1, t._2 - p._2, t._3 - p._3)
    def *(s: Long) = (t._1 * s, t._2 * s, t._3 * s)
    def /(s: Long) = (t._1 / s, t._2 / s, t._3 / s)
  } 
  
  def posAtTime(t: Long): (Long, Long, Long) = {
    //distance = 1/2 * a * t^2 + v*t
    position + (velocity * t) + (accel * t * t / 2)
  }

  def collidesWith(p: Particle): Option[Long] = {
    def straightRoot(v: Long, p: Long): List[Long] = {
      if (v == 0 && p == 0) List(-1L)
      else if (v == 0) List[Long]()
      else {
        val t = (-p.toDouble / v)
        if (t.isValidInt && t >= 0) List(t.toLong)
        else List[Long]()
      }
    }
    def quadraticRoot(a: Long, b: Long, c: Long): List[Long] = {
      if (b * b - 4 * a * c < 0) List[Long]()
      else { 
        val zeros = List((-b + math.sqrt(b * b - 4 * a * c)) / (2 * a),
                         (-b - math.sqrt(b * b - 4 * a * c)) / (2 * a))
        zeros.filter(r => r.isValidInt && r >= 0).map(_.toLong)
      }
    }

    def dimRoot(a: Long, v: Long, p: Long): List[Long] = {
      if (a == 0) straightRoot(v, p)
      else quadraticRoot(a, v, p)
    }

    val (accelDiff, velDiff, posDiff) = ((accel- p.accel),
                                         (velocity - p.velocity) * 2,
                                         (position - p.position) * 2) 

    val rootList = List(dimRoot(accelDiff._1, accelDiff._1 + velDiff._1, posDiff._1),
                        dimRoot(accelDiff._2, accelDiff._2 + velDiff._2, posDiff._2),
                        dimRoot(accelDiff._3, accelDiff._3 + velDiff._3, posDiff._3))
 
    val rootMaybe = rootList.filter(_ != List(-1)).reduce((l, r) => l.intersect(r))

    if (rootMaybe.isEmpty) None
    else Some(rootMaybe.min)
  }
}

object Particles {
  def parse(str: String): Particle = {
    val regex = raw"p=<([-+]?\d+),([-+]?\d+),([-+]?\d+)>, v=<([-+]?\d+),([-+]?\d+),([-+]?\d+)>, a=<([-+]?\d+),([-+]?\d+),([-+]?\d+)>".r

    val regex(px, py, pz, vx, vy, vz, ax, ay, az) = str

    new Particle((px.toLong, py.toLong, pz.toLong),
                 (vx.toLong, vy.toLong, vz.toLong),
                 (ax.toLong, ay.toLong, az.toLong))
  }

  def manhatten(position: (Long, Long, Long)): Long = {
    math.abs(position._1) + math.abs(position._2) + math.abs(position._3)
  }

  def run(particleData: List[String]) {
    val particles = particleData.map(parse)

    val closest = particles.map(_.accel)
      .map(manhatten)
      .zipWithIndex
      .sortBy(_._1)
      .groupBy(_._1)
      .minBy(_._1)._2.map(idx => (particles(idx._2).posAtTime(1000L), idx._2))
      .map(p => (manhatten(p._1), p._2))
      .minBy(_._1)
      ._2
      
    println(closest)

    val collisions = particles.zipWithIndex.combinations(2)
      .map(combo => {
        val collideMaybe = combo(0)._1.collidesWith(combo(1)._1)
        if (collideMaybe.isEmpty) None
        else Some(((combo(0)._2, combo(1)._2), collideMaybe.get))
      }).flatten.toList.sortBy(_._2)


    val collisionMap = collisions.groupBy(_._2).mapValues(pairList => {
      pairList.map(pair => pair._1)
    })

    val aliveParticles = collisionMap.toList.sortBy(_._1).map(_._2)
      .foldLeft(Stream.continually(true).take(particles.length).toVector)(
        (activeParticles, collideList) => {
          collideList.filter(collide =>
              activeParticles(collide._1) && activeParticles(collide._2)
          ).map(c => List(c._1, c._2)).flatten.toSet
          .foldLeft(activeParticles)((active, deadParticle) =>
              active.updated(deadParticle, false)
          )
        }
      ).count(_ == true)

    println(aliveParticles)
  }

  def main(args: Array[String]) {
    val data = scala.io.Source.stdin.getLines.toList

    run(data)
  }
}
