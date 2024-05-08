package sh.miles.totem.util.serialized.recipe

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonObject
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ShapedRecipe
import sh.miles.pineapple.PineappleLib
import sh.miles.pineapple.util.serialization.SerializedDeserializeContext
import sh.miles.pineapple.util.serialization.SerializedObject
import sh.miles.totem.api.TotemItem
import sh.miles.totem.api.TotemRecipe
import sh.miles.totem.api.impl.TotemRecipeImpl

object ShapedRecipeParser : RecipeParser {

    override fun recipeTime(
        parent: SerializedObject,
        key: NamespacedKey,
        result: TotemItem,
        context: SerializedDeserializeContext
    ): TotemRecipe {
        val shaped = ShapedRecipe(key, result.icon)
        val shapeElement = PineappleLib.getAnomalyFactory().create()
            .run { parent.getArray("shape").orThrow() }
            .message("The given shaped recipe MUST have a shape array")
            .hard(javaClass, "recipeTime").orThrow()

        val rows = mutableListOf<String>()
        rows.add(shapeElement.get(0)!!.asPrimitive.asString + shapeElement.get(1)!!.asPrimitive.asString + shapeElement.get(2)!!.asPrimitive.asString)
        rows.add(shapeElement.get(3)!!.asPrimitive.asString + shapeElement.get(4)!!.asPrimitive.asString + shapeElement.get(5)!!.asPrimitive.asString)
        rows.add(shapeElement.get(6)!!.asPrimitive.asString + shapeElement.get(7)!!.asPrimitive.asString + shapeElement.get(8)!!.asPrimitive.asString)

        shaped.shape(*rows.toTypedArray())

        val ingredients = PineappleLib.getAnomalyFactory().create()
            .run { parent.getObject("ingredients").orThrow() }
            .message("A ShapedRecipe must have a ingredients object which provides key value matching from the shape")
            .hard(javaClass, "recipeTime").orThrow()

        for (keyString in ingredients.keySet()) {
            val keyChar = keyString[0]
            shaped.setIngredient(keyChar, RecipeParser.readChoice(ingredients.getOrNull(keyString)!!, context))
        }

        return TotemRecipeImpl(result, shaped.ingredientMap.values.toTypedArray(), shaped)
    }
}
