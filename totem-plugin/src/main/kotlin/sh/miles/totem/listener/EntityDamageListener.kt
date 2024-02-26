package sh.miles.totem.listener

import org.bukkit.EntityEffect
import org.bukkit.Material
import org.bukkit.entity.LivingEntity
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.persistence.PersistentDataType
import sh.miles.totem.api.impl.TotemItemImpl
import sh.miles.totem.registry.TotemItemRegistry
import sh.miles.totem.util.TotemInventoryUtils

@Suppress("UnstableApiUsage")
class EntityDamageListener : Listener {

    @EventHandler
    fun onEntityTakeDamage(event: EntityDamageEvent) {
        val entity = event.entity
        if (entity !is LivingEntity || entity.health - event.damage > 0) {
            return
        }

        val equipment = entity.equipment ?: return
        val damageType = event.damageSource.damageType
        val firstTotem = TotemInventoryUtils.getEquipmentSlotsWithTarget(Material.TOTEM_OF_UNDYING, equipment)
            .stream()
            .filter {
                val meta = equipment.getItem(it).itemMeta!!
                return@filter meta.persistentDataContainer.has(TotemItemImpl.ITEM_KEY)
            }.map {
                val key = equipment.getItem(it).itemMeta!!.persistentDataContainer.get(
                    TotemItemImpl.ITEM_KEY,
                    PersistentDataType.STRING
                )!!
                return@map Pair(it, TotemItemRegistry.get(key).orElseThrow())
            }.filter { it.second.settings.blockTypes.contains(damageType) }
            .findFirst()

        // TODO: refactor. Maybe add trigger to API?
        firstTotem.ifPresent {
            val totem = it.second
            if (totem is TotemItemImpl) {
                totem.trigger(entity, it.first, true)
            }
            event.isCancelled = true

            val source = event.damageSource
            val yaw = if (source.damageLocation != null) source.damageLocation!!.yaw else 0.0F
            entity.playHurtAnimation(yaw)
        }
    }

}
