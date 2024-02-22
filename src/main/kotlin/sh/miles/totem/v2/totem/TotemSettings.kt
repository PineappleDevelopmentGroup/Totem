package sh.miles.totem.v2.totem

import org.bukkit.damage.DamageType
import org.bukkit.potion.PotionEffect
import sh.miles.pineapple.collection.registry.RegistryKey

data class TotemSettings(
    private val key: String,
    val blockedType: Set<DamageType>,
    val givenEffects: List<PotionEffect>,
    val standardEffects: Boolean
) : RegistryKey<String> {
    class Builder {
        private var key: String? = null
        private val blockedTypes = mutableSetOf<DamageType>()
        private val givenEffects = mutableListOf<PotionEffect>()
        private var standardEffects: Boolean = false

        fun key(key: String) = apply { this.key = key }
        fun blockType(damageType: DamageType) = apply { this.blockedTypes.add(damageType) }
        fun giveEffect(effect: PotionEffect) = apply { this.givenEffects.add(effect) }
        fun standardEffects() = apply { this.standardEffects = true }
    }

    override fun getKey(): String {
        return this.key
    }
}
