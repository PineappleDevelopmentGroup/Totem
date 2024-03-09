package sh.miles.totem.json.recipe

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.Recipe
import org.bukkit.inventory.RecipeChoice
import sh.miles.totem.api.TotemItem
import sh.miles.totem.api.TotemRecipe

interface RecipeParser {

    companion object {
        fun readChoice(element: JsonElement, context: JsonDeserializationContext): RecipeChoice {
            return if (element is JsonPrimitive) { // Assume material
                RecipeChoice.MaterialChoice(Material.matchMaterial(element.asString))
            } else {
                RecipeChoice.ExactChoice(context.deserialize<ItemStack>(element, ItemStack::class.java))
            }
        }
    }

    fun recipeTime(parent: JsonObject, key: NamespacedKey, result: TotemItem, context: JsonDeserializationContext): TotemRecipe

}
