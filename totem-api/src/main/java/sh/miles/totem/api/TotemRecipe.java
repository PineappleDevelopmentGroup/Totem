package sh.miles.totem.api;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.jetbrains.annotations.NotNull;
import sh.miles.pineapple.collection.registry.RegistryKey;

/**
 * Represents a TotemRecipe. TotemRecipes are registered with bukkit so you can craft Totems
 *
 * @since 1.2.0-SNAPSHOT
 */
public interface TotemRecipe extends RegistryKey<String> {

    /**
     * Gets the Recipe Input
     *
     * @return the recipe inputs
     * @since 1.2.0-SNAPSHOT
     */
    @NotNull
    ItemStack[] getInput();

    /**
     * Gets the TotemItem result of this recipe
     *
     * @return the result
     * @since 1.2.0-SNAPSHOT
     */
    @NotNull
    TotemItem getResult();

    /**
     * Gets the Bukkit Recipe
     *
     * @return the bukkit recipe
     * @since 1.2.0-SNAPSHOT
     */
    @NotNull
    Recipe getRecipe();

}
