package sh.miles.totem.command

import sh.miles.pineapple.command.Command
import sh.miles.pineapple.command.CommandLabel

class TotemCommand : Command(CommandLabel("totem", "totem.command")) {

    init {
        registerSubcommand(TotemGiveCommand())
    }

}
