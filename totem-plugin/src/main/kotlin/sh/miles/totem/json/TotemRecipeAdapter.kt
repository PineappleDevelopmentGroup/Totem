package sh.miles.totem.json

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.BlastingRecipe
import org.bukkit.inventory.FurnaceRecipe
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.Recipe
import org.bukkit.inventory.RecipeChoice
import org.bukkit.inventory.RecipeChoice.ExactChoice
import org.bukkit.inventory.RecipeChoice.MaterialChoice
import org.bukkit.inventory.ShapedRecipe
import org.bukkit.inventory.ShapelessRecipe
import org.bukkit.inventory.SmokingRecipe
import sh.miles.pineapple.json.JsonAdapter
import sh.miles.totem.registry.TotemItemRegistry
import java.lang.reflect.Type

object TotemRecipeAdapter : JsonAdapter<Recipe> {
    override fun serialize(recipe: Recipe, type: Type, context: JsonSerializationContext): JsonElement {
        throw IllegalArgumentException("Recipes can not yet be serialized")
    }

    override fun deserialize(element: JsonElement, type: Type, context: JsonDeserializationContext): Recipe {
        val parent = element.asJsonObject
        val recipeTypeElement = parent.get("recipe_type")
            ?: throw IllegalArgumentException("the given recipe must have a recipe_type specified")
        val recipeType = RecipeType.fromString(recipeTypeElement.asString)
        val resultElement =
            parent.get("result") ?: throw IllegalArgumentException("The given recipe must have a result specified")
        val result = TotemItemRegistry.getOrNull(resultElement.asString) ?: throw IllegalArgumentException("The given totem-item key is not valid")
        val keyElement = parent.get("key") ?: throw IllegalArgumentException("The given recipe must have a key")
        val key = NamespacedKey.fromString("totem:" + keyElement.asString)!!

        return createRecipe(parent, context, recipeType, key, result.icon)
    }

    override fun getAdapterType(): Class<Recipe> {
        return Recipe::class.java
    }

    override fun isHierarchy(): Boolean {
        return true
    }

    fun createRecipe(
        parent: JsonObject,
        context: JsonDeserializationContext,
        recipeType: RecipeType,
        key: NamespacedKey,
        result: ItemStack
    ): Recipe {
        return when (recipeType) {
            RecipeType.SHAPED -> createShaped(parent, context, key, result)
            RecipeType.SHAPELESS -> createShapeless(parent, context, key, result)
            RecipeType.FURNACE -> createFurnaceLike(parent, context, key, result, ::FurnaceRecipe)
            RecipeType.BLAST_FURNACE -> createFurnaceLike(parent, context, key, result, ::BlastingRecipe)
            RecipeType.SMOKER -> createFurnaceLike(parent, context, key, result, ::SmokingRecipe)
        }
    }

    private fun createShaped(parent: JsonObject, context: JsonDeserializationContext, key: NamespacedKey, result: ItemStack): Recipe {
        val recipe = ShapedRecipe(key, result)
        val shapeElement =
            parent.getAsJsonArray("shape") ?: throw IllegalArgumentException("A ShapedRecipe must have a shape array")

        val strings = mutableListOf<String>()
        strings.add(shapeElement.get(0).asString + shapeElement.get(1).asString + shapeElement.get(2).asString)
        strings.add(shapeElement.get(3).asString + shapeElement.get(4).asString + shapeElement.get(5).asString)
        strings.add(shapeElement.get(6).asString + shapeElement.get(7).asString + shapeElement.get(8).asString)

        recipe.shape(*strings.toTypedArray())

        val ingredients = parent.getAsJsonObject("ingredients") ?: throw IllegalArgumentException("A ShapedRecipe must have a ingredients object which provides key value matching from the shape")
        for (keyString in ingredients.keySet()) {
            val keyChar = keyString[0]
            recipe.setIngredient(keyChar, readChoice(keyChar, ingredients, context))
        }

        return recipe
    }

    private fun createShapeless(parent: JsonObject, context: JsonDeserializationContext, key: NamespacedKey, result: ItemStack): ShapelessRecipe {
        val recipe = ShapelessRecipe(key, result)
        val ingredients = parent.getAsJsonArray("ingredients") ?: throw IllegalArgumentException("A Shapeless recipe must have an ingredients array!")
        for (element in ingredients) {
            if (element is JsonPrimitive) { // Assume material
                recipe.addIngredient(MaterialChoice(Material.matchMaterial(element.asString)))
            } else {
                recipe.addIngredient(ExactChoice(context.deserialize<ItemStack>(element, ItemStack::class.java)))
            }
        }

        return recipe
    }

    private fun createFurnaceLike(parent: JsonObject, context: JsonDeserializationContext, key: NamespacedKey, result: ItemStack, builder: (NamespacedKey, ItemStack, RecipeChoice, Float, Int) -> Recipe): Recipe {
        val input = readChoice(parent.get("input") ?: throw IllegalArgumentException("A furnace recipe must have an input!"), context)
        val cookTime = if (parent.has("cook_time")) parent.get("cook_time").asFloat else 100f
        val experience = if (parent.has("experience")) parent.get("experience").asInt else 10
        return builder.invoke(key, result, input, cookTime, experience)
    }

    private fun readChoice(element: JsonElement, context: JsonDeserializationContext): RecipeChoice {
        return if (element is JsonPrimitive) { // Assume material
            MaterialChoice(Material.matchMaterial(element.asString))
        } else {
            ExactChoice(context.deserialize<ItemStack>(element, ItemStack::class.java))
        }
    }

    private fun readChoice(key: Char, parent: JsonObject, context: JsonDeserializationContext): RecipeChoice {
        if (!parent.has(key.toString())) {
            throw IllegalArgumentException("The character $key is not mapped so its recipe choice could not be read")
        }
        val choice = parent.get(key.toString())

        return if (choice is JsonPrimitive) { // Assume material
            MaterialChoice(Material.matchMaterial(choice.asString))
        } else {
            ExactChoice(context.deserialize<ItemStack>(choice, ItemStack::class.java))
        }
    }

    enum class RecipeType {
        SHAPED,
        SHAPELESS,
        FURNACE,
        BLAST_FURNACE,
        SMOKER;

        companion object {
            fun fromString(str: String): RecipeType {
                try {
                    return valueOf(str.uppercase())
                } catch (exception: IllegalArgumentException) {
                    throw IllegalArgumentException("The given string $str is not a valid recipe type")
                }
            }
        }
    }
}
