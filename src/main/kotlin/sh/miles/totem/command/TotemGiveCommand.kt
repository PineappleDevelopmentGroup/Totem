package sh.miles.totem.command

import org.bukkit.NamespacedKey
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import sh.miles.pineapple.command.Command
import sh.miles.pineapple.command.CommandLabel
import sh.miles.totem.registry.TotemTypeRegistry

internal class TotemGiveCommand : Command(CommandLabel("give", "totem.command.give")) {

    override fun execute(sender: CommandSender, args: Array<out String>): Boolean {
        if (sender !is Player) {
            sender.sendMessage("Only players can send this command")
            return true;
        }

        if (args.isEmpty()) {
            sender.sendMessage("/totem give <totem-type>")
            return true;
        }

        val totemType = TotemTypeRegistry.get(NamespacedKey.fromString(args[0])!!)
        if (totemType.isEmpty) {
            sender.sendMessage("that totem type doesn't exist")
            return true;
        }

        sender.inventory.addItem(totemType.orElseThrow().item())
        sender.sendMessage("Given totem")
        return true;
    }

    override fun complete(sender: CommandSender, args: Array<out String>): MutableList<String> {
        if (args.size == 1) {
            return TotemTypeRegistry.keys().stream().map { it.toString() }.toList()
        }

        return mutableListOf()
    }

}
