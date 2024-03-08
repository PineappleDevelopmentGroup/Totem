package sh.miles.totem.registry

import org.bukkit.Bukkit
import sh.miles.pineapple.collection.registry.WriteableRegistry
import sh.miles.totem.TotemPlugin
import sh.miles.totem.api.TotemRecipe

private const val FILE_NAME: String = "totem-recipes.json"

object TotemRecipeRegistry : WriteableRegistry<TotemRecipe, String>({
    TotemPlugin.plugin.jsonHelper.asArray(
        TotemPlugin.plugin, FILE_NAME, Array<TotemRecipe>::class.java
    ).associateBy { it.key }.toMap()
}) {
    const val TOTEM_RECIPES_NAME = FILE_NAME

    override fun register(`object`: TotemRecipe): Boolean {
        Bukkit.addRecipe(`object`.recipe)
        return super.register(`object`)
    }
}
