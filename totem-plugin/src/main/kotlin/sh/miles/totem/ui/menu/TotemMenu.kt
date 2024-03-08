package sh.miles.totem.ui.menu

import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import sh.miles.pineapple.PineappleLib
import sh.miles.pineapple.gui.PlayerGui
import sh.miles.pineapple.nms.annotations.NMS
import sh.miles.pineapple.nms.api.menu.MenuType
import sh.miles.pineapple.nms.api.menu.scene.MenuScene
import sh.miles.totem.TotemConfig
import sh.miles.totem.api.TotemItem
import sh.miles.totem.registry.TotemItemRegistry
import java.lang.IllegalStateException

@NMS
class TotemMenu(player: Player) : PlayerGui<MenuScene>({
    val inventoryRows = TotemItemRegistry.keys().size / 9
    if (inventoryRows >= 6) {
        PineappleLib.getAnomalyFactory().create()
            .useExceptionMessage()
            .noThrowLog()
            .run { throw IllegalStateException("The amount of totems created in the config is too big to fit within 54 slots!") }
            .soft(TotemMenu::class.java, "Constructor")
    } else if (inventoryRows == 0) {
        PineappleLib.getAnomalyFactory().create()
            .useExceptionMessage()
            .run { throw IllegalStateException("You must specify at least one totem to use the TotemMenu 9 slots!") }
            .hard(TotemMenu::class.java, "Constructor")
    }

    MenuType.fromRows(inventoryRows).create(it, TotemConfig.TOTEM_MENU_TITLE.component())
}, player) {

    override fun decorate() {
        TODO("Not yet implemented")
    }

    fun processTotemClick(totemItem: TotemItem, event: InventoryClickEvent) {
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

    fun processRightClick(totemItem: TotemItem, event: InventoryClickEvent) {
        val viewer = viewer();
    }
}
