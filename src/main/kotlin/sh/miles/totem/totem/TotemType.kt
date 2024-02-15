package sh.miles.totem.totem

import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.Registry
import org.bukkit.Statistic
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityResurrectEvent
import org.bukkit.inventory.EntityEquipment
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import sh.miles.pineapple.PineappleLib
import sh.miles.pineapple.collection.registry.RegistryKey
import sh.miles.totem.registry.TotemTypeRegistry
import java.util.Optional

class TotemType(private val key: NamespacedKey, val settings: TotemSettings, item: ItemStack) :
    RegistryKey<NamespacedKey> {

    private val item: ItemStack

    init {
        val meta = item.itemMeta!!
        meta.persistentDataContainer.set(TYPE_KEY, PersistentDataType.STRING, key.toString())
        item.itemMeta = meta
        this.item = item
    }

    fun item(): ItemStack {
        return this.item.clone()
    }

    fun trigger(entity: LivingEntity, consume: Boolean): Boolean {
        val slot = getTotemEquipmentSlot(
            entity.equipment ?: error("Unexpected error totem was found but entity has no equipment?")
        ) ?: error("Unexpected error totem was found but entity has no totem equipped?")

        val event = EntityResurrectEvent(entity, slot)
        Bukkit.getPluginManager().callEvent(event)
        if (event.isCancelled) {
            return false
        }

        if (consume) {
            entity.equipment!!.setItem(slot, ItemStack(Material.AIR))
        }

        if (entity is Player) {
            entity.incrementStatistic(Statistic.RAID_TRIGGER)
            val advancement = entity.getAdvancementProgress(Registry.ADVANCEMENT.get(TOTEM_ADVANCEMENT)!!)
            if (!advancement.isDone) {
                advancement.awardCriteria("used_totem")
            }
        }

        entity.health = 1.0
        entity.activePotionEffects.forEach { entity.removePotionEffect(it.type) }
        if (settings.standardEffects) {
            entity.addPotionEffect(PotionEffect(PotionEffectType.REGENERATION, 900, 1))
            entity.addPotionEffect(PotionEffect(PotionEffectType.ABSORPTION, 100, 1))
            entity.addPotionEffect(PotionEffect(PotionEffectType.FIRE_RESISTANCE, 800, 0))
        }
        settings.givenEffects.forEach { entity.addPotionEffect(it) }
        PineappleLib.getNmsProvider().packets.broadcastTotemEffect(entity)
        return true
    }

    companion object {
        private val TYPE_KEY = NamespacedKey.fromString("totem:totem-type")!!
        private val TOTEM_ADVANCEMENT = NamespacedKey.minecraft("adventure/totem_of_undying")

        fun hasTotemType(entity: LivingEntity): Boolean {
            val equipment = entity.equipment ?: return false
            val equipmentSlot = getTotemEquipmentSlot(equipment) ?: return false

            val item = equipment.getItem(equipmentSlot)
            if (item.type.isAir) return false

            val container = item.itemMeta!!.persistentDataContainer
            return container.has(TYPE_KEY)
        }

        fun getType(entity: LivingEntity): Optional<TotemType> {
            val slot = getTotemEquipmentSlot(entity.equipment ?: return Optional.empty<TotemType>())
                ?: return Optional.empty<TotemType>()
            return getType(entity.equipment!!.getItem(slot))
        }

        fun getType(item: ItemStack): Optional<TotemType> {
            if (item.type.isAir) {
                return Optional.empty()
            }

            val container = item.itemMeta!!.persistentDataContainer
            if (!container.has(TYPE_KEY)) {
                return Optional.empty()
            }

            val key = NamespacedKey.fromString(container.get(TYPE_KEY, PersistentDataType.STRING)!!)!!
            return TotemTypeRegistry.get(key)
        }

        private fun getTotemEquipmentSlot(equipment: EntityEquipment): EquipmentSlot? {
            if (equipment.itemInMainHand.type == Material.TOTEM_OF_UNDYING) {
                return EquipmentSlot.HAND
            } else if (equipment.itemInOffHand.type == Material.TOTEM_OF_UNDYING) {
                return EquipmentSlot.OFF_HAND
            }

            return null
        }
    }


    override fun getKey(): NamespacedKey {
        return key
    }

}
