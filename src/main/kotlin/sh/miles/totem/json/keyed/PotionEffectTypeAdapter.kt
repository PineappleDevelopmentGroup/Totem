package sh.miles.totem.json.keyed

import org.bukkit.Registry
import org.bukkit.potion.PotionEffectType
import sh.miles.totem.json.KeyedJsonAdapter

internal object PotionEffectTypeAdapter : KeyedJsonAdapter<PotionEffectType> {

    override fun registry(): Registry<PotionEffectType> = Registry.EFFECT

    override fun getAdapterType(): Class<PotionEffectType> {
        return PotionEffectType::class.java
    }
}
