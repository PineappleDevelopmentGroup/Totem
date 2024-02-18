package sh.miles.totem.menu

import org.bukkit.entity.Player
import sh.miles.pineapple.gui.PlayerGui
import sh.miles.pineapple.gui.slot.GuiSlot
import sh.miles.pineapple.nms.api.menu.MenuType
import sh.miles.pineapple.nms.api.menu.scene.MenuScene
import sh.miles.totem.TotemConfig
import sh.miles.totem.registry.TotemTypeRegistry
import sh.miles.totem.totem.TotemType
import java.lang.IllegalStateException

class TotemMenu(viewer: Player) :
    PlayerGui<MenuScene>({
        calculateMenuType(TotemTypeRegistry.keys().size).create(
            it,
            TotemConfig.TOTEM_MENU_TILE.component()
        )
    }, viewer) {
    companion object {
        private fun calculateMenuType(registrySize: Int): MenuType<*> {
            val size = registrySize / 9 + 1
            return when (size) {
                1 -> MenuType.GENERIC_9x1
                2 -> MenuType.GENERIC_9x2
                3 -> MenuType.GENERIC_9x3
                4 -> MenuType.GENERIC_9x4
                5 -> MenuType.GENERIC_9x5
                6 -> MenuType.GENERIC_9x6
                else -> throw IllegalStateException("can not create menu of bigger size than 54")
            }
        }
    }

    override fun decorate() {
        for ((index, namespacedKey) in TotemTypeRegistry.keys().withIndex()) {
            println(index)
            addTotem(index, TotemTypeRegistry.getOrNull(namespacedKey)!!)
        }
    }

    private fun addTotem(index: Int, totemType: TotemType) {
        slot(index) { inventory ->
            GuiSlot.GuiSlotBuilder()
                .inventory(inventory)
                .index(index)
                .item(totemType.item())
                .click() {
                    it.isCancelled = true
                    it.whoClicked.inventory.addItem(totemType.item())
                }.build()
        }
    }
}
