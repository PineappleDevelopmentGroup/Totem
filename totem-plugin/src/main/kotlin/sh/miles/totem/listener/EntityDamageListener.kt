package sh.miles.totem.listener

import org.bukkit.entity.LivingEntity
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityResurrectEvent
import sh.miles.pineapple.function.Option.None
import sh.miles.pineapple.function.Option.Some
import sh.miles.totem.totem.TotemType
import java.lang.IllegalStateException
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
        when (val totemBundle = TotemType.getEquippedTotems(entity)) {
            is Some -> {
                val bundle = totemBundle.some()
                bundle.tryTriggerStored(damageSource.damageType, entity, true)
            }

            is None -> {
                throw IllegalStateException("Totem type was clearly found but a totem bundle could not be gathered this is a bug!")
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
