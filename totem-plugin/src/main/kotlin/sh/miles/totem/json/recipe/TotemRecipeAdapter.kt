package sh.miles.totem.json.recipe

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonElement
import com.google.gson.JsonSerializationContext
import org.bukkit.NamespacedKey
import org.bukkit.inventory.BlastingRecipe
import org.bukkit.inventory.FurnaceRecipe
import org.bukkit.inventory.SmokingRecipe
import sh.miles.pineapple.PineappleLib
import sh.miles.pineapple.json.JsonAdapter
import sh.miles.totem.api.TotemRecipe
import sh.miles.totem.registry.TotemItemRegistry
import java.lang.reflect.Type

object TotemRecipeAdapter : JsonAdapter<TotemRecipe> {

    override fun serialize(p0: TotemRecipe, p1: Type, p2: JsonSerializationContext): JsonElement {
        throw IllegalArgumentException("Not currently supported!")
    }

    override fun deserialize(element: JsonElement, type: Type, context: JsonDeserializationContext): TotemRecipe {
        val parent = element.asJsonObject
        val recipeType = PineappleLib.getAnomalyFactory().create()
            .run { RecipeType.fromString(parent.get("recipe_type").asString) }
            .message(
                "The given recipe_type must be one of the following ${
                    RecipeType.entries.map { it.name }.toTypedArray()
                }"
            )
            .hard(javaClass, "deserialize").orThrow()

        val totemResult = PineappleLib.getAnomalyFactory().create()
            .run { TotemItemRegistry.getOrNull(parent.get("result").asString)!! }
            .message("The totem of type ${parent.get("result")} does not exist!")
            .hard(javaClass, "deserialize").orThrow()

        val key = PineappleLib.getAnomalyFactory().create()
            .run { NamespacedKey.fromString("totem:" + parent.get("key").asString)!! }
            .message("Could not find key field for totem recipe! This field is REQUIRED")
            .hard(javaClass, "deserialize").orThrow()

        return recipeType.parser.recipeTime(parent, key, totemResult, context)
    }

    override fun getAdapterType(): Class<TotemRecipe> {
        return TotemRecipe::class.java
    }

    override fun isHierarchy(): Boolean {
        return true
    }

    enum class RecipeType(val parser: RecipeParser) {
        SHAPED(ShapedRecipeParser),
        SHAPELESS(ShapelessRecipeParser),
        FURNACE(FurnaceRecipeParser(::FurnaceRecipe)),
        BLAST_FURNACE(FurnaceRecipeParser(::BlastingRecipe)),
        SMOKER(FurnaceRecipeParser(::SmokingRecipe));

        companion object {
            fun fromString(str: String): RecipeType {
                try {
                    return RecipeType.valueOf(str.uppercase())
                } catch (exception: IllegalArgumentException) {
                    throw IllegalArgumentException("The given string $str is not a valid recipe type")
                }
            }
        }
    }

}
