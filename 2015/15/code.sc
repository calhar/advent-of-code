case class Ingredient(name: String, cap: Int, dur: Int, flavor: Int, texture: Int, cal: Int) {
  def score(quantity: Int): List[Int] =
    List(cap, dur, flavor, texture).map(_ * quantity)
}

class Recipe(val ingredients: List[Ingredient], val amounts: List[Int]) {
  def score: Int = {
    amounts.zip(ingredients).map(pair => {
      pair._2.score(pair._1)
    }).transpose
    .map(propertyScores => propertyScores.sum)
    .map(propertyScore => if (propertyScore > 0) propertyScore else 0).product
  }

  def bestAddition: Recipe = {
    Stream.continually(amounts)
      .take(amounts.length)
      .zipWithIndex
      .map({ case (l, idx) =>
        l.updated(idx, l(idx) + 1)
      }).map(newAmounts =>
        Recipe(ingredients, newAmounts)
      ).maxBy(_.score)
  }
}

object Recipe {
  def apply(ingredients: List[Ingredient]): Recipe = {
    Recipe(ingredients, List.fill(ingredients.length)(0))
  }

  def apply(ingredients: List[Ingredient], amounts: List[Int]): Recipe = {
    new Recipe(ingredients, amounts)
  }

  private val ingredientRegex = raw"""(\w+):(?:,? \w+ ([-+]?\d+))(?:,? \w+ ([-+]?\d+))(?:,? \w+ ([-+]?\d+))(?:,? \w+ ([-+]?\d+))(?:,? \w+ ([-+]?\d+))""".r
  def parseIngredients(str: String): Ingredient = {
    val ingredientRegex(name, cap, dur, flavor, texture, cal) = str
    Ingredient(name, cap.toInt, dur.toInt, flavor.toInt, texture.toInt, cal.toInt)
  }

  def run(ingredientStrs: Iterator[String]) {
    val ingredients = ingredientStrs.map(parseIngredients).toList

    val bestRecipe = Iterator.iterate(Recipe(ingredients))(_.bestAddition)
      .drop(100)
      .next
    println(bestRecipe.score)
  }

  def main(args: Array[String]) {
    val data = io.Source.stdin.getLines

    run(data)
  }
}
