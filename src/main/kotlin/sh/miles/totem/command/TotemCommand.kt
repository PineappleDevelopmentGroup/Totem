package sh.miles.totem.command

import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import sh.miles.pineapple.command.Command
import sh.miles.pineapple.command.CommandLabel
import sh.miles.totem.TotemConfig
import sh.miles.totem.menu.TotemMenu

class TotemCommand : Command(CommandLabel("totem", "totem.command")) {

    init {
        registerSubcommand(TotemGiveCommand())
        registerSubcommand(object : Command(CommandLabel("menu", "totem.command.menu")) {
            override fun execute(sender: CommandSender, args: Array<out String>): Boolean {
                if (sender !is Player) {
                    sender.spigot().sendMessage(TotemConfig.PLAYER_ONLY_COMMAND.component())
                    return true
                }

                if (sender.hasPermission(commandLabel.permission)) {
                    TotemMenu(sender).open()
                } else {
                    sender.sendMessage("No permission")
                }
                return true
            }
        })
    }

}
