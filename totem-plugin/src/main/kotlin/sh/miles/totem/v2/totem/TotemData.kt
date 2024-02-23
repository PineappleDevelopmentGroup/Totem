package sh.miles.totem.v2.totem

import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import sh.miles.pineapple.collection.registry.RegistryKey

class TotemData(private val key: String, val settings: TotemSettings, private var itemBase: ItemStack) :
    RegistryKey<String> {

    val item: ItemStack
        get() {
            return itemBase.clone()
        }

    init {
        itemBase = itemBase.clone()
        val meta = itemBase.itemMeta!!
        meta.persistentDataContainer.set(Totem.TOTEM_DATA_KEY, PersistentDataType.STRING, key)
        itemBase.itemMeta = meta
    }

    override fun getKey(): String {
        return this.key
    }
}
