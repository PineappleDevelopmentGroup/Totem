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
import sh.miles.pineapple.function.Option
import sh.miles.pineapple.function.Option.None
import sh.miles.pineapple.function.Option.Some
import sh.miles.totem.registry.TotemTypeRegistry
import sh.miles.totem.util.InventoryUtils
import java.util.Optional
import java.util.stream.Collectors

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

    fun trigger(entity: LivingEntity, slot: EquipmentSlot, consume: Boolean): Boolean {
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

        fun getEquippedTotems(entity: LivingEntity): Option<TotemBundle> {
            val equipment = entity.equipment ?: return Option.none()
            val target = InventoryUtils.getEquipmentSlotsWithTarget(Material.TOTEM_OF_UNDYING, equipment)
            return Option.some(TotemBundle(target.stream().map {
                return@map Pair(it, getType(equipment.getItem(it)))
            }.filter { it.second is Some }.map { Pair(it.first, it.second.orThrow()) }
                .collect(Collectors.toUnmodifiableList())))
        }

        fun getType(item: ItemStack): Option<TotemType> {
            if (item.type.isAir) {
                return Option.none()
            }

            val container = item.itemMeta!!.persistentDataContainer
            if (!container.has(TYPE_KEY)) {
                return Option.none()
            }

            val key = NamespacedKey.fromString(container.get(TYPE_KEY, PersistentDataType.STRING)!!)!!
            return Option.some(TotemTypeRegistry.getOrNull(key) ?: return Option.none())
        }

        fun hasTotemType(entity: LivingEntity): Boolean {
            val equipment = entity.equipment ?: return false
            val equipmentSlots = InventoryUtils.getEquipmentSlotsWithTarget(Material.TOTEM_OF_UNDYING, equipment)
            if (equipmentSlots.isEmpty()) {
                return false
            }

            for (equipmentSlot in equipmentSlots) {
                val item = equipment.getItem(equipmentSlot)
                if (item.type.isAir) continue

                val container = item.itemMeta!!.persistentDataContainer
                return container.has(TYPE_KEY)
            }

            return false
        }
    }


    override fun getKey(): NamespacedKey {
        return key
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is TotemType) return false

        if (key != other.key) return false
        if (settings != other.settings) return false
        if (item != other.item) return false

        return true
    }

    override fun hashCode(): Int {
        var result = key.hashCode()
        result = 31 * result + settings.hashCode()
        result = 31 * result + item.hashCode()
        return result
    }

}
