package sh.miles.totem.json.keyed

import org.bukkit.Registry
import org.bukkit.damage.DamageType
import sh.miles.totem.json.KeyedJsonAdapter

@Suppress("UnstableApiUsage")
internal object DamageTypeAdapter : KeyedJsonAdapter<DamageType> {

    override fun registry(): Registry<DamageType> = Registry.DAMAGE_TYPE

    override fun getAdapterType(): Class<DamageType> {
        return DamageType::class.java
    }
}
