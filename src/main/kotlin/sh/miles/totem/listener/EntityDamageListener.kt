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

        println("before has totem check")
        if (!TotemType.hasTotemType(entity)) return
        println("passed has totem check")

        val damageSource = event.damageSource
        val totem = TotemType.getType(entity)
        totem.ifPresent { totemType ->
            println("totem type is present")
            if (totemType.settings.blockedType.contains(damageSource.damageType)) {
                println("blocked damage is allowed")
                if (totemType.trigger(entity, true)) event.isCancelled = true
                println("event cancellation status ${event.isCancelled}")
            }

            if (!event.isCancelled) {
                println("No protection granted")
                noSavior.add(entity.uniqueId)
            }
        }
    }

    @EventHandler
    fun onEntityAttemptResurrect(event: EntityResurrectEvent) {
        if (noSavior.contains(event.entity.uniqueId)) {
            println("The Gods decided not protect this entity")
            event.isCancelled = true
            noSavior.remove(event.entity.uniqueId)
        }
    }

}
