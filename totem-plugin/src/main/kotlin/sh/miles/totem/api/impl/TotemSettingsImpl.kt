package sh.miles.totem.api.impl

import com.google.common.base.Preconditions
import org.bukkit.damage.DamageType
import org.bukkit.potion.PotionEffect
import sh.miles.totem.api.TotemSettings

@Suppress("UnstableApiUsage")
class TotemSettingsImpl(
    private val key: String,
    givenBlockedTypes: Set<DamageType>,
    givenPotionEffects: Set<PotionEffect>,
    private val applyStandardEffects: Boolean,
    private val playReviveParticles: Boolean
) : TotemSettings {

    private val blockedTypes: Set<DamageType> = givenBlockedTypes.toSet()
    private val givenEffects: Set<PotionEffect> = givenPotionEffects.toSet()

    override fun getKey(): String {
        return key
    }

    override fun getBlockTypes(): Set<DamageType> {
        return blockedTypes
    }

    override fun getGivenEffects(): Set<PotionEffect> {
        return givenEffects
    }

    override fun isApplyStandardEffects(): Boolean {
        return applyStandardEffects
    }

    override fun isPlayReviveParticle(): Boolean {
        return playReviveParticles
    }

    class Builder : TotemSettings.Builder {

        private val blockedTypes = mutableSetOf<DamageType>()
        private val givenEffects = mutableSetOf<PotionEffect>()
        private var applyStandard = false
        private var playParticle = false

        override fun addBlockedType(damageType: DamageType): TotemSettings.Builder {
            blockedTypes.add(damageType)
            return this
        }

        override fun addGivenEffect(potionEffect: PotionEffect): TotemSettings.Builder {
            givenEffects.add(potionEffect)
            return this
        }

        override fun doApplyStandardEffects(): TotemSettings.Builder {
            applyStandard = true
            return this
        }

        override fun doPlayReviveParticle(): TotemSettings.Builder {
            playParticle = true
            return this
        }

        override fun build(key: String): TotemSettings {
            Preconditions.checkArgument(key != null, "The given key must not be null")
            return TotemSettingsImpl(key, blockedTypes, givenEffects, applyStandard, playParticle)
        }

    }
}
