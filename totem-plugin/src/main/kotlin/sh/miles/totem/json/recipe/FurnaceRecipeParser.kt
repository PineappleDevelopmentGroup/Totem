package sh.miles.totem.json.recipe

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonObject
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.Recipe
import org.bukkit.inventory.RecipeChoice
import org.bukkit.inventory.RecipeChoice.ExactChoice
import org.bukkit.inventory.RecipeChoice.MaterialChoice
import sh.miles.pineapple.PineappleLib
import sh.miles.totem.api.TotemItem
import sh.miles.totem.api.TotemRecipe
import sh.miles.totem.api.impl.TotemRecipeImpl

class FurnaceRecipeParser(private val recipeBuilder: (NamespacedKey, ItemStack, RecipeChoice, Float, Int) -> Recipe) :
    RecipeParser {
    override fun recipeTime(
        parent: JsonObject, key: NamespacedKey, result: TotemItem, context: JsonDeserializationContext
    ): TotemRecipe {
        val input =
            PineappleLib.getAnomalyFactory().create().run { RecipeParser.readChoice(parent.get("input"), context) }
                .message("A furnace recipe must have a json field input!").hard(javaClass, "recipeTime").orThrow()
        val cookTime = if (parent.has("cook_time")) parent.get("cook_time").asFloat else 100f
        val experience = if (parent.has("experience")) parent.get("experience").asInt else 10

        if (input is MaterialChoice) {
            return TotemRecipeImpl(
                result,
                input.choices.map { ItemStack(it) }.toTypedArray(),
                recipeBuilder.invoke(key, result.icon, input, cookTime, experience)
            )
        } else {
            val itemInput = input as ExactChoice
            return TotemRecipeImpl(
                result,
                itemInput.choices.toTypedArray(),
                recipeBuilder.invoke(key, result.icon, input, cookTime, experience)
            )
        }

    }
}
