package sh.miles.totem.json.recipe

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonObject
import org.bukkit.NamespacedKey
import org.bukkit.inventory.Recipe
import org.bukkit.inventory.ShapedRecipe
import sh.miles.pineapple.PineappleLib
import sh.miles.totem.api.TotemItem
import sh.miles.totem.api.TotemRecipe
import sh.miles.totem.api.impl.TotemRecipeImpl

object ShapedRecipeParser : RecipeParser {

    override fun recipeTime(
        parent: JsonObject,
        key: NamespacedKey,
        result: TotemItem,
        context: JsonDeserializationContext
    ): TotemRecipe {
        val shaped = ShapedRecipe(key, result.icon)
        val shapeElement = PineappleLib.getAnomalyFactory().create()
            .run { parent.getAsJsonArray("shape") }
            .message("The given shaped recipe MUST have a shape array")
            .hard(javaClass, "recipeTime").orThrow()

        val rows = mutableListOf<String>()
        rows.add(shapeElement.get(0).asString + shapeElement.get(1).asString + shapeElement.get(2).asString)
        rows.add(shapeElement.get(3).asString + shapeElement.get(4).asString + shapeElement.get(5).asString)
        rows.add(shapeElement.get(6).asString + shapeElement.get(7).asString + shapeElement.get(8).asString)

        shaped.shape(*rows.toTypedArray())

        val ingredients = PineappleLib.getAnomalyFactory().create()
            .run { parent.getAsJsonObject("ingredients") }
            .message("A ShapedRecipe must have a ingredients object which provides key value matching from the shape")
            .hard(javaClass, "recipeTime").orThrow()

        for (keyString in ingredients.keySet()) {
            val keyChar = keyString[0]
            shaped.setIngredient(keyChar, RecipeParser.readChoice(ingredients.get(keyString), context))
        }

        return TotemRecipeImpl(result, shaped.ingredientMap.values.toTypedArray(), shaped)
    }
}
