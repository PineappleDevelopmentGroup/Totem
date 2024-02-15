package sh.miles.totem.totem

import org.bukkit.NamespacedKey
import org.bukkit.Particle
import org.bukkit.damage.DamageType
import org.bukkit.potion.PotionEffect
import sh.miles.pineapple.collection.registry.RegistryKey

@Suppress("UnstableApiUsage")
data class TotemSettings(
    private val key: NamespacedKey,
    val blockedType: List<DamageType>,
    val givenEffects: List<PotionEffect>,
    val particleType: Particle,
    val standardEffects: Boolean
) : RegistryKey<NamespacedKey> {
    override fun getKey(): NamespacedKey {
        return this.key
    }
}
