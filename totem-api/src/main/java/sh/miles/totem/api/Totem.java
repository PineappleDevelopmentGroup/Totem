package sh.miles.totem.api;

import org.jetbrains.annotations.NotNull;
import sh.miles.pineapple.collection.registry.WriteableRegistry;

/**
 * The API interface for Totem
 *
 * @since 1.0.0-SNAPSHOT
 */
public interface Totem {

    /**
     * Gets the totem settings registry
     *
     * @return the totem settings registry
     * @since 1.0.0-SNAPSHOT
     */
    @NotNull
    WriteableRegistry<TotemSettings, String> getTotemSettingsRegistry();

    /**
     * Gets the totem item registry
     *
     * @return the totem item registry
     * @since 1.0.0-SNAPSHOT
     */
    @NotNull
    WriteableRegistry<TotemItem, String> getTotemItemRegistry();

    /**
     * Gets the totem recipe registry
     *
     * @return the totem item registry
     * @since 1.2.0-SNAPSHOT
     */
    @NotNull
    WriteableRegistry<TotemRecipe, String> getTotemRecipeRegistry();

    /**
     * Creates a TotemItem Builder
     *
     * @return the totem item builder
     * @since 1.0.0-SNAPSHOT
     */
    @NotNull
    TotemItem.Builder buildTotemItem();

    /**
     * Creates a TotemSettings Builder
     *
     * @return the totem settings builder
     * @since 1.0.0-SNAPSHOT
     */
    @NotNull
    TotemSettings.Builder buildTotemSettings();
}
