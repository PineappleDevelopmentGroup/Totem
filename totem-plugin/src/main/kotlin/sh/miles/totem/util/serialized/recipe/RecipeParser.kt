package sh.miles.totem.util.serialized.recipe

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.RecipeChoice
import sh.miles.pineapple.util.serialization.SerializedDeserializeContext
import sh.miles.pineapple.util.serialization.SerializedElement
import sh.miles.pineapple.util.serialization.SerializedObject
import sh.miles.pineapple.util.serialization.SerializedPrimitive
import sh.miles.totem.api.TotemItem
import sh.miles.totem.api.TotemRecipe

interface RecipeParser {

    companion object {
        fun readChoice(element: SerializedElement, context: SerializedDeserializeContext): RecipeChoice {
            return if (element is SerializedPrimitive) { // Assume material
                RecipeChoice.MaterialChoice(Material.matchMaterial(element.asString))
            } else {
                RecipeChoice.ExactChoice(context.deserialize(element, ItemStack::class.java))
            }
        }
    }

    fun recipeTime(
        parent: SerializedObject,
        key: NamespacedKey,
        result: TotemItem,
        context: SerializedDeserializeContext
    ): TotemRecipe

}
