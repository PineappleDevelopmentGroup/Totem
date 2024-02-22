package sh.miles.totem.v2.totem

import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import sh.miles.pineapple.function.Option
import sh.miles.totem.v2.registry.TotemDataRegistry
import sh.miles.totem.v2.registry.TotemSettingsRegistry

object Totem {
    const val TOTEM_SETTINGS_REGISTRY_FILE_NAME = "totem-settings.json"
    const val TOTEM_DATA_REGISTRY_FILE_NAME = "totem-data.json"

    val TOTEM_DATA_KEY = NamespacedKey.fromString("totem:totem_data")!!

    val DATA_REGISTRY = TotemDataRegistry
    val SETTINGS_REGISTRY = TotemSettingsRegistry

    fun getTotemData(item: ItemStack): Option<TotemData> {
        if (item.type.isAir) {
            return Option.none()
        }
        val container = item.itemMeta!!.persistentDataContainer
        if (!container.has(TOTEM_DATA_KEY)) {
            return Option.none()
        }

        return Option.some(DATA_REGISTRY.getOrNull(container.get(TOTEM_DATA_KEY, PersistentDataType.STRING)!!)!!)
    }


}
