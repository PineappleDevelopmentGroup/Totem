package sh.miles.totem.api.impl

import com.google.common.base.Preconditions
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.Registry
import org.bukkit.Statistic
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityResurrectEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import sh.miles.pineapple.PineappleLib
import sh.miles.totem.api.TotemItem
import sh.miles.totem.api.TotemSettings

class TotemItemImpl(private val key: String, icon: ItemStack, private val settings: TotemSettings) :
    TotemItem {

    private val item: ItemStack

    init {
        val temp = icon.clone()
        val meta = temp.itemMeta!!
        meta.persistentDataContainer.set(ITEM_KEY, PersistentDataType.STRING, key)
        temp.setItemMeta(meta)

        this.item = temp
    }

    companion object {
        val ITEM_KEY: NamespacedKey = NamespacedKey.fromString("totem:totem_item")!!
        val TOTEM_ADVANCEMENT = NamespacedKey.minecraft("adventure/totem_of_undying")
    }

    fun trigger(entity: LivingEntity, slot: EquipmentSlot, consume: Boolean) {
        val event = EntityResurrectEvent(entity, slot)
        Bukkit.getPluginManager().callEvent(event)
        if (event.isCancelled) {
            return
        }

        if (consume) {
            entity.equipment!!.setItem(slot, ItemStack(Material.AIR))
        }

        if (entity is Player) {
            entity.incrementStatistic(Statistic.USE_ITEM, Material.TOTEM_OF_UNDYING)
            val advancement = entity.getAdvancementProgress(Registry.ADVANCEMENT.get(TOTEM_ADVANCEMENT)!!)
            if (!advancement.isDone) {
                advancement.awardCriteria("ued_totem")
            }
        }

        entity.health = 1.0
        entity.activePotionEffects.forEach { entity.removePotionEffect(it.type) }
        if (settings.isApplyStandardEffects) {
            entity.addPotionEffect(PotionEffect(PotionEffectType.REGENERATION, 900, 1))
            entity.addPotionEffect(PotionEffect(PotionEffectType.ABSORPTION, 100, 1))
            entity.addPotionEffect(PotionEffect(PotionEffectType.FIRE_RESISTANCE, 800, 0))
        }
        settings.givenEffects.forEach { entity.addPotionEffect(it) }

        if (settings.isPlayReviveParticle) {
            PineappleLib.getNmsProvider().packets.broadcastTotemEffect(entity)
        }
    }

    override fun getKey(): String {
        return key
    }

    override fun getIcon(): ItemStack {
        return item.clone()
    }

    override fun getSettings(): TotemSettings {
        return settings
    }

    class Builder : TotemItem.Builder {
        private lateinit var item: ItemStack
        private lateinit var settings: TotemSettings

        override fun item(item: ItemStack): TotemItem.Builder {
            this.item = item
            return this
        }

        override fun settings(settings: TotemSettings): TotemItem.Builder {
            this.settings = settings
            return this
        }

        override fun build(key: String): TotemItem {
            Preconditions.checkArgument(key != null, "The given key must not null")
            Preconditions.checkState(this.item != null, "the given item must not be null")
            Preconditions.checkState(this.settings != null, "The given settings must not be null")

            return TotemItemImpl(key, item, settings)
        }

    }
}
