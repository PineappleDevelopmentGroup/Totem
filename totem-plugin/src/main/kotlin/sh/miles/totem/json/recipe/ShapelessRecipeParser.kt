package sh.miles.totem.json.recipe

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonObject
import org.bukkit.NamespacedKey
import org.bukkit.inventory.Recipe
import org.bukkit.inventory.ShapelessRecipe
import sh.miles.pineapple.PineappleLib
import sh.miles.totem.api.TotemItem
import sh.miles.totem.api.TotemRecipe
import sh.miles.totem.api.impl.TotemRecipeImpl

object ShapelessRecipeParser : RecipeParser {
    override fun recipeTime(
        parent: JsonObject,
        key: NamespacedKey,
        result: TotemItem,
        context: JsonDeserializationContext
    ): TotemRecipe {
        val shapeless = ShapelessRecipe(key, result.icon)
        val ingredients = PineappleLib.getAnomalyFactory().create()
            .run { parent.getAsJsonArray("ingredients") }
            .message("A Shapeless recipe must have an ingredients array!")
            .hard(javaClass, "recipeTime").orThrow()

        for (element in ingredients) {
            shapeless.addIngredient(RecipeParser.readChoice(element, context))
        }

        return TotemRecipeImpl(result, shapeless.ingredientList.toTypedArray(), shapeless)
    }
}
