package sh.miles.totem.command

import org.bukkit.NamespacedKey
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import sh.miles.pineapple.command.Command
import sh.miles.pineapple.command.CommandLabel
import sh.miles.totem.TotemConfig
import sh.miles.totem.registry.TotemTypeRegistry

internal class TotemGiveCommand : Command(CommandLabel("give", "totem.command.give")) {

    override fun execute(sender: CommandSender, args: Array<out String>): Boolean {
        if (sender !is Player) {
            sender.spigot().sendMessage(TotemConfig.PLAYER_ONLY_COMMAND.component())
            return true;
        }

        if (args.isEmpty()) {
            sender.spigot().sendMessage(TotemConfig.TOTEM_GIVE_USAGE.component())
            return true;
        }

        val totemType = TotemTypeRegistry.get(NamespacedKey.fromString(args[0])!!)
        if (totemType.isEmpty) {
            sender.spigot().sendMessage(TotemConfig.TOTEM_TYPE_NOT_EXISTS.component(mapOf("totem-type" to args[0])))
            return true;
        }

        sender.inventory.addItem(totemType.orElseThrow().item())
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

    override fun complete(sender: CommandSender, args: Array<out String>): MutableList<String> {
        if (args.size == 1) {
            return TotemTypeRegistry.keys().stream().map { it.toString() }.toList()
        }

        return mutableListOf()
    }

}
