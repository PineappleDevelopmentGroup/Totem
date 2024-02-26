package sh.miles.totem.api;

import org.bukkit.damage.DamageType;
import org.bukkit.potion.PotionEffect;
import org.jetbrains.annotations.NotNull;
import sh.miles.pineapple.collection.registry.RegistryKey;

import java.util.Set;

/**
 * Expresses the settings a totem can have
 *
 * @since 1.0.0-SNAPSHOT
 */
public interface TotemSettings extends RegistryKey<String> {
    /**
     * Gets the types of damage this totem protects from
     *
     * @return a set of damage types
     * @since 1.0.0-SNAPSHOT
     */
    @SuppressWarnings("UnstableApiUsage")
    Set<DamageType> getBlockTypes();

    /**
     * Gets the potion effects given on revival
     *
     * @return a set of potion effects
     * @since 1.0.0-SNAPSHOT
     */
        Set<PotionEffect> getGivenEffects();

    /**
     * Dictates whether to apply the default potion effects when you are revived.
     * <p>
     * Note that the standard effects are applied on top of the effects from {@link #getGivenEffects()}
     *
     * @return true if standard effects should be applied, otherwise false
     * @since 1.0.0-SNAPSHOT
     */
    boolean isApplyStandardEffects();

    /**
     * Determines whether or not to play the totem revive particles to the player
     *
     * @return true to play particles, otherwise false
     * @since 1.0.0-SNAPSHOT
     */
    boolean isPlayReviveParticle();

    /**
     * Operates on Building TotemSettings
     *
     * @since 1.0.0-SNAPSHOT
     */
    interface Builder {
        /**
         * Adds a new blocked type to this builder
         *
         * @param damageType the damage type
         * @return this builder
         * @since 1.0.0-SNAPSHOT
         */
        @SuppressWarnings("UnstableApiUsage")
        Builder addBlockedType(@NotNull DamageType damageType);

        /**
         * Adds a potion effect to this builder
         *
         * @param potionEffect the potion effect
         * @return this builder
         * @since 1.0.0-SNAPSHOT
         */
        Builder addGivenEffect(@NotNull PotionEffect potionEffect);

        /**
         * Sets true the apply standard effects
         *
         * @return this builder
         * @since 1.0.0-SNAPSHOT
         */
        Builder doApplyStandardEffects();

        /**
         * Sets true the totem particles
         *
         * @return this builder
         * @since 1.0.0-SNAPSHOT
         */
        Builder doPlayReviveParticle();

        /**
         * Builds the totem settings
         *
         * @param key the unique TotemSettings key
         * @return the settings
         * @throws IllegalArgumentException if no blocked types or given effects are
         * @since 1.0.0-SNAPSHOT
         */
        TotemSettings build(@NotNull final String key);
    }
}

