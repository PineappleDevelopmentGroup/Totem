package sh.miles.totem.ui.command

import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import sh.miles.pineapple.command.Command
import sh.miles.pineapple.command.CommandLabel
import sh.miles.pineapple.function.Option.None
import sh.miles.pineapple.function.Option.Some
import sh.miles.totem.TotemConfig
import sh.miles.totem.registry.TotemItemRegistry

class TotemGiveCommand : Command(CommandLabel("give", "totem.command.give")) {

    override fun execute(sender: CommandSender, args: Array<out String>): Boolean {
        if (sender !is Player) {
            sender.spigot().sendMessage(TotemConfig.PLAYER_ONLY_COMMAND.component())
            return true;
        }

        if (args.isEmpty()) {
            sender.spigot().sendMessage(TotemConfig.TOTEM_GIVE_USAGE.component())
            return true;
        }

        when (val totemType = TotemItemRegistry.get(args[0])) {
            is Some -> {
                sender.inventory.addItem(totemType.some().icon)
                sender.spigot().sendMessage(
                    TotemConfig.TOTEM_GIVE_SUCCESS.component(
                        mapOf(
                            "totem-type" to args[0],
                            "player-name" to sender.displayName,
                        )
                    )
                )
                return true;
            }

            is None -> {
                sender.spigot().sendMessage(TotemConfig.TOTEM_TYPE_NOT_EXISTS.component(mapOf("totem-type" to args[0])))
                return true;
            }
        }
    }

    override fun complete(sender: CommandSender, args: Array<out String>): MutableList<String> {
        if (args.size == 1) {
            return TotemItemRegistry.keys().stream().map { it.toString() }.toList()
        }

        return mutableListOf()
    }

}
