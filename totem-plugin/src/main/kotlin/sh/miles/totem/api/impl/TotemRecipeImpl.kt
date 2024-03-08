package sh.miles.totem.api.impl

import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.Recipe
import sh.miles.totem.api.TotemItem
import sh.miles.totem.api.TotemRecipe

class TotemRecipeImpl(
    private val result: TotemItem,
    private val input: Array<ItemStack>,
    private val recipe: Recipe
) : TotemRecipe {

    override fun getKey(): String {
        return result.key
    }

    override fun getInput(): Array<ItemStack> {
        return input.clone()
    }

    override fun getResult(): TotemItem {
        return result
    }

    override fun getRecipe(): Recipe {
        return recipe
    }
}
