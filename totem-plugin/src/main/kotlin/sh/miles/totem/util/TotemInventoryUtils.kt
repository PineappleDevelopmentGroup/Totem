package sh.miles.totem.util

import org.bukkit.Material
import org.bukkit.inventory.EntityEquipment
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack

object TotemInventoryUtils {

    fun getEquipmentSlotsWithTarget(material: Material, equipment: EntityEquipment): List<EquipmentSlot> {
        return entityEquipmentContents(equipment).stream().filter { it.second.type == material }.map { it.first }
            .toList()
    }

    fun getEquipmentSlotsWithSimilarTarget(itemStack: ItemStack, equipment: EntityEquipment): List<EquipmentSlot> {
        return entityEquipmentContents(equipment).stream().filter { it.second.isSimilar(itemStack) }.map { it.first }
            .toList()
    }

    fun entityEquipmentContents(equipment: EntityEquipment): List<Pair<EquipmentSlot, ItemStack>> {
        return listOf(
            Pair(EquipmentSlot.HAND, equipment.itemInMainHand ?: air()),
            Pair(EquipmentSlot.OFF_HAND, equipment.itemInOffHand ?: air()),
            Pair(EquipmentSlot.HEAD, equipment.helmet ?: air()),
            Pair(EquipmentSlot.CHEST, equipment.chestplate ?: air()),
            Pair(EquipmentSlot.LEGS, equipment.leggings ?: air()),
            Pair(EquipmentSlot.FEET, equipment.boots ?: air())
        )
    }

    private fun air(): ItemStack {
        return ItemStack(Material.AIR)
    }
}
