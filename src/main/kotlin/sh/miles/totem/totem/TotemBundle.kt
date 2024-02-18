package sh.miles.totem.totem

import org.bukkit.damage.DamageType
import org.bukkit.entity.LivingEntity
import org.bukkit.inventory.EquipmentSlot

class TotemBundle(types: List<Pair<EquipmentSlot, TotemType>>) {

    private val storedTypes: MutableList<Pair<EquipmentSlot, TotemType>> = ArrayList()

    init {
        this.storedTypes.addAll(types)
    }

    fun addStoredType(equipmentSlot: EquipmentSlot, totemType: TotemType) {
        this.storedTypes.add(Pair(equipmentSlot, totemType))
    }

    fun removeStoredType(equipmentSlot: EquipmentSlot, totemType: TotemType) {
        this.storedTypes.remove(Pair(equipmentSlot, totemType))
    }

    fun tryTriggerStored(damageType: DamageType, entity: LivingEntity, consume: Boolean) {
        var triggered: Pair<EquipmentSlot, TotemType>? = null
        for (storedType in storedTypes) {
            if (storedType.second.settings.blockedType.contains(damageType)) {
                triggered = storedType
                break
            }
        }

        if (triggered != null) {
            triggered.second.trigger(entity, triggered.first, consume)
            storedTypes.remove(triggered)
        }
    }

}
