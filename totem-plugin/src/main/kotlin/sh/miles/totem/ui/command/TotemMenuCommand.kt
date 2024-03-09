package sh.miles.totem.ui.command

import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import sh.miles.pineapple.command.Command
import sh.miles.pineapple.command.CommandLabel
import sh.miles.totem.TotemConfig
import sh.miles.totem.ui.menu.TotemMenu

class TotemMenuCommand : Command(CommandLabel("menu", "totem.command.menu")) {

    override fun execute(sender: CommandSender, args: Array<out String>): Boolean {
        if (sender !is Player) {
            sender.spigot().sendMessage(TotemConfig.PLAYER_ONLY_COMMAND.component())
            return true
        }

        TotemMenu(sender).open()
        return true
    }

}
