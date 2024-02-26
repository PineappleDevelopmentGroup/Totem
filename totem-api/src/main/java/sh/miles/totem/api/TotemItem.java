package sh.miles.totem.api;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import sh.miles.pineapple.collection.registry.RegistryKey;

/**
 * Represents a TotemItem that can be copied and distributed
 *
 * @since 1.0.0-SNAPSHOT
 */
public interface TotemItem extends RegistryKey<String> {

    /**
     * Gets a copy of the TotemItem icon
     *
     * @return the ItemStack
     * @since 1.0.0-SNAPSHOT
     */
    @NotNull ItemStack getIcon();

    /**
     * Gets the settings for this TotemItem
     *
     * @return the totem settings
     * @since 1.0.0-SNAPSHOT
     */
    @NotNull TotemSettings getSettings();

    /**
     * Builds TotemItems
     *
     * @since 1.0.0-SNAPSHOT
     */
    interface Builder {
        /**
         * Sets the item on this builder
         *
         * @param item the item
         * @return this builder
         * @since 1.0.0-SNAPSHOT
         */
        Builder item(@NotNull final ItemStack item);

        /**
         * Sets the settings on this builder
         *
         * @param settings the settings
         * @return this builder
         * @since 1.0.0-SNAPSHOT
         */
        Builder settings(@NotNull final TotemSettings settings);

        /**
         * Builds this TotemItem
         *
         * @param key the key
         * @return the totem item
         * @throws IllegalStateException    thrown if item or settings is not set
         * @throws IllegalArgumentException if the given key is null
         * @since 1.0.0-SNAPSHOT
         */
        TotemItem build(@NotNull final String key);
    }
}
