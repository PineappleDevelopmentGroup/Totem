package sh.miles.totem.ui.menu

import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.ItemStack
import sh.miles.pineapple.gui.PlayerGui
import sh.miles.pineapple.nms.api.menu.MenuType
import sh.miles.pineapple.nms.api.menu.scene.MenuScene
import sh.miles.totem.TotemConfig
import sh.miles.totem.TotemPlugin
import sh.miles.totem.api.TotemItem
import sh.miles.totem.api.TotemRecipe
import sh.miles.totem.registry.TotemRecipeRegistry
import java.util.Locale

private val BACKGROUND = ItemStack(Material.BLACK_STAINED_GLASS_PANE)
private val ARROW_ITEM = ItemStack(Material.LIME_STAINED_GLASS_PANE)
private val INPUT = arrayOf(11, 12, 10, 1, 2, 3, 19, 20, 21)
private val ARROW = arrayOf(13, 14, 15)
private const val OUTPUT = 16

class TotemRecipeMenu(private val totemItem: TotemItem, private val totemMenu: TotemMenu, player: Player) :
    PlayerGui<MenuScene>({
        MenuType.GENERIC_9x3.create(
            player, TotemConfig.TOTEM_RECIPE_MENU_TITLE.component(
                mapOf("totem-type" to totemItem.key.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() })
            )
        )
    }, player) {

    private val recipe: TotemRecipe?;

    init {
        this.recipe = TotemRecipeRegistry.getOrNull(totemItem.key)
    }

    override fun decorate() {
        val recipe = this.recipe;
        if (recipe == null) {
            close()
            return
        }

        for (i in 0 until size()) {
            slot(i).item = BACKGROUND
        }
        for (i in ARROW) {
            slot(i).item = ARROW_ITEM
        }

        for ((index, itemStack) in recipe.input.withIndex()) {
            val inputIndex = INPUT[index]
            slot(inputIndex).item = itemStack
        }

        slot(OUTPUT).item = totemItem.icon
    }

    override fun close() {
        super.close()
        totemMenu.open()
    }

    override fun handleClose(event: InventoryCloseEvent) {
        super.handleClose(event)
        Bukkit.getScheduler().runTask(TotemPlugin.plugin, Runnable {
            totemMenu.open()
        })
    }

}
