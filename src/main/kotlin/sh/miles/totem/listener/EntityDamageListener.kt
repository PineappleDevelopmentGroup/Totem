package sh.miles.totem.listener

import org.bukkit.entity.LivingEntity
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityResurrectEvent
import sh.miles.totem.totem.TotemType
import java.util.UUID

@Suppress("UnstableApiUsage")
class EntityDamageListener : Listener {

    private val noSavior = mutableSetOf<UUID>()

    @EventHandler
    fun onEntityDamage(event: EntityDamageEvent) {
        val entity = event.entity
        if (entity !is LivingEntity || entity.health - event.damage > 0) {
            return
        }

        if (!TotemType.hasTotemType(entity)) return

        val damageSource = event.damageSource
        val totem = TotemType.getType(entity)
        totem.ifPresent { totemType ->
            if (totemType.settings.blockedType.contains(damageSource.damageType)) {
                if (totemType.trigger(entity, true)) event.isCancelled = true
            }

            if (!event.isCancelled) {
                noSavior.add(entity.uniqueId)
            }
        }
    }

    @EventHandler
    fun onEntityAttemptResurrect(event: EntityResurrectEvent) {
        if (noSavior.contains(event.entity.uniqueId)) {
            event.isCancelled = true
            noSavior.remove(event.entity.uniqueId)
        }
    }

}
