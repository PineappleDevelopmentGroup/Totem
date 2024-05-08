package sh.miles.totem.util.serialized.recipe

import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.Recipe
import org.bukkit.inventory.RecipeChoice
import org.bukkit.inventory.RecipeChoice.ExactChoice
import org.bukkit.inventory.RecipeChoice.MaterialChoice
import sh.miles.pineapple.PineappleLib
import sh.miles.pineapple.util.serialization.SerializedDeserializeContext
import sh.miles.pineapple.util.serialization.SerializedObject
import sh.miles.totem.api.TotemItem
import sh.miles.totem.api.TotemRecipe
import sh.miles.totem.api.impl.TotemRecipeImpl

class FurnaceRecipeParser(private val recipeBuilder: (NamespacedKey, ItemStack, RecipeChoice, Float, Int) -> Recipe) :
    RecipeParser {
    override fun recipeTime(
        parent: SerializedObject, key: NamespacedKey, result: TotemItem, context: SerializedDeserializeContext
    ): TotemRecipe {
        val input =
            PineappleLib.getAnomalyFactory().create().run { RecipeParser.readChoice(parent.get("input").orThrow(), context) }
                .message("A furnace recipe must have a json field input!").hard(javaClass, "recipeTime").orThrow()
        val cookTime = if (parent.has("cook_time")) parent.get("cook_time").orThrow().asPrimitive.asDouble else 100f
        val experience = if (parent.has("experience")) parent.get("experience").orThrow().asPrimitive.asInt else 10

        if (input is MaterialChoice) {
            return TotemRecipeImpl(
                result,
                input.choices.map { ItemStack(it) }.toTypedArray(),
                recipeBuilder.invoke(key, result.icon, input, cookTime.toFloat(), experience)
            )
        } else {
            val itemInput = input as ExactChoice
            return TotemRecipeImpl(
                result,
                itemInput.choices.toTypedArray(),
                recipeBuilder.invoke(key, result.icon, input, cookTime.toFloat(), experience)
            )
        }
    }
}
