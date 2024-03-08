package sh.miles.totem.ui.command

import sh.miles.pineapple.command.Command
import sh.miles.pineapple.command.CommandLabel

class TotemCommand : Command(CommandLabel("totem", "totem.command")) {

    init {
        registerSubcommand(TotemGiveCommand())
    }

}
