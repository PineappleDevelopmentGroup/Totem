package sh.miles.totem.util.serialized.recipe

import org.bukkit.NamespacedKey
import org.bukkit.inventory.ShapelessRecipe
import sh.miles.pineapple.PineappleLib
import sh.miles.pineapple.util.serialization.SerializedDeserializeContext
import sh.miles.pineapple.util.serialization.SerializedObject
import sh.miles.totem.api.TotemItem
import sh.miles.totem.api.TotemRecipe
import sh.miles.totem.api.impl.TotemRecipeImpl

object ShapelessRecipeParser : RecipeParser {
    override fun recipeTime(
        parent: SerializedObject,
        key: NamespacedKey,
        result: TotemItem,
        context: SerializedDeserializeContext
    ): TotemRecipe {
        val shapeless = ShapelessRecipe(key, result.icon)
        val ingredients = PineappleLib.getAnomalyFactory().create()
            .run { parent.getArray("ingredients").orThrow() }
            .message("A Shapeless recipe must have an ingredients array!")
            .hard(javaClass, "recipeTime").orThrow()

        for (element in ingredients) {
            shapeless.addIngredient(RecipeParser.readChoice(element, context))
        }

        return TotemRecipeImpl(result, shapeless.ingredientList.toTypedArray(), shapeless)
    }
}
