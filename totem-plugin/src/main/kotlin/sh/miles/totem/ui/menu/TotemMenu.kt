package sh.miles.totem.ui.menu

import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack
import sh.miles.pineapple.PineappleLib
import sh.miles.pineapple.gui.PlayerGui
import sh.miles.pineapple.gui.slot.SimpleGuiSlot
import sh.miles.pineapple.nms.annotations.NMS
import sh.miles.pineapple.nms.api.menu.MenuType
import sh.miles.pineapple.nms.api.menu.scene.MenuScene
import sh.miles.totem.TotemConfig
import sh.miles.totem.api.TotemItem
import sh.miles.totem.registry.TotemItemRegistry
import java.lang.IllegalStateException

@NMS
class TotemMenu(player: Player) : PlayerGui<MenuScene>({
    var inventoryRows = TotemItemRegistry.keys().size / 9
    if (inventoryRows >= 6) {
        PineappleLib.getAnomalyFactory().create()
            .useExceptionMessage()
            .noThrowLog()
            .run { throw IllegalStateException("The amount of totems created in the config is too big to fit within 54 slots!") }
            .soft(TotemMenu::class.java, "Constructor")
    } else if (TotemItemRegistry.keys().size == 0) {
        PineappleLib.getAnomalyFactory().create()
            .useExceptionMessage()
            .run { throw IllegalStateException("You must specify at least one totem to use the TotemMenu 9 slots!") }
            .hard(TotemMenu::class.java, "Constructor")
    } else if (inventoryRows == 0) {
        inventoryRows = 1
    }

    MenuType.fromRows(inventoryRows).create(it, TotemConfig.TOTEM_MENU_TITLE.component())
}, player) {

    override fun decorate() {
        for (index in 0 until size()) {
            slot(index) {
                val slot = SimpleGuiSlot(it, index) { event ->
                    event.isCancelled = true
                }
                slot.item = ItemStack(Material.BLACK_STAINED_GLASS_PANE)

                return@slot slot
            }
        }
        for ((index, key) in TotemItemRegistry.keys().withIndex()) {
            val item = TotemItemRegistry.get(key).orElseThrow()
            slot(index) {
                val slot = SimpleGuiSlot(it, index) { event ->
                    event.isCancelled = true
                    if (event.isRightClick) {
                        processRightClick(item, event)
                    } else if (event.isLeftClick) {
                        processLeftClick(item, event)
                    }
                }
                slot.item = item.icon

                return@slot slot
            }
        }
    }

    private fun processLeftClick(totemItem: TotemItem, event: InventoryClickEvent) {
        val viewer = viewer()
        if (!viewer.hasPermission("totem.command.give")) {
            return
        }

        viewer.inventory.addItem(totemItem.icon)
        TotemConfig.TOTEM_GIVE_SUCCESS.component(
            mapOf(
                "totem-type" to totemItem.key,
                "player-name" to viewer.displayName,
            )
        )
    }

    private fun processRightClick(totemItem: TotemItem, event: InventoryClickEvent) {
        val viewer = viewer();
        val recipeMenu = TotemRecipeMenu(totemItem, this, viewer)
        recipeMenu.open()
    }
}
