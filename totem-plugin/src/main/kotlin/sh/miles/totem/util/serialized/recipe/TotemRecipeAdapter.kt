package sh.miles.totem.util.serialized.recipe

import org.bukkit.NamespacedKey
import org.bukkit.inventory.BlastingRecipe
import org.bukkit.inventory.FurnaceRecipe
import org.bukkit.inventory.SmokingRecipe
import sh.miles.pineapple.PineappleLib
import sh.miles.pineapple.util.serialization.SerializedDeserializeContext
import sh.miles.pineapple.util.serialization.SerializedElement
import sh.miles.pineapple.util.serialization.SerializedSerializeContext
import sh.miles.pineapple.util.serialization.adapter.SerializedAdapter
import sh.miles.totem.api.TotemRecipe
import sh.miles.totem.registry.TotemItemRegistry

object TotemRecipeAdapter : SerializedAdapter<TotemRecipe> {

    override fun serialize(recipe: TotemRecipe, context: SerializedSerializeContext): SerializedElement {
        throw IllegalArgumentException("Not currently supported!")
    }

    override fun deserialize(element: SerializedElement, context: SerializedDeserializeContext): TotemRecipe {
        val parent = element.asObject
        val recipeType = PineappleLib.getAnomalyFactory().create()
            .run { RecipeType.fromString(parent.getPrimitive("recipe_type").orThrow().asString) }
            .message(
                "The given recipe_type must be one of the following ${
                    RecipeType.entries.map { it.name }.toTypedArray()
                }"
            )
            .hard(javaClass, "deserialize").orThrow()

        val totemResult = PineappleLib.getAnomalyFactory().create()
            .run { TotemItemRegistry.getOrNull(parent.getPrimitive("result").orThrow().asString)!! }
            .message("The totem of type ${parent.get("result")} does not exist!")
            .hard(javaClass, "deserialize").orThrow()

        val key = PineappleLib.getAnomalyFactory().create()
            .run { NamespacedKey.fromString("totem:" + parent.getPrimitive("key").orThrow().asString)!! }
            .message("Could not find key field for totem recipe! This field is REQUIRED")
            .hard(javaClass, "deserialize").orThrow()

        return recipeType.parser.recipeTime(parent, key, totemResult, context)
    }

    override fun getKey(): Class<*> {
        return TotemRecipe::class.java
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
