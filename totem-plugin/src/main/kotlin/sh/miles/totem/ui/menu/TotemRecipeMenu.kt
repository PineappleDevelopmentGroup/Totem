package sh.miles.totem.ui.menu

import org.bukkit.entity.Player
import sh.miles.pineapple.gui.PlayerGui
import sh.miles.pineapple.nms.api.menu.MenuType
import sh.miles.pineapple.nms.api.menu.scene.MenuScene
import sh.miles.totem.TotemConfig
import sh.miles.totem.api.TotemItem
import java.util.Locale

class TotemRecipeMenu(private val totemItem: TotemItem, private val totemMenu: TotemMenu, player: Player) :
    PlayerGui<MenuScene>({
        MenuType.GENERIC_9x3.create(
            player, TotemConfig.TOTEM_RECIPE_MENU_TITLE.component(
                mapOf("totem-type" to totemItem.key.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() })
            )
        )
    }, player) {

    override fun decorate() {
        TODO("Not yet implemented")
    }

    override fun close() {
        super.close()
        totemMenu.open()
    }

}
