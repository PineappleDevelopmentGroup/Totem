package sh.miles.totem

import sh.miles.pineapple.chat.PineappleChat
import sh.miles.pineapple.config.annotation.ConfigEntry

class TotemConfig {

    companion object {
        @ConfigEntry("player-only-command")
        val PLAYER_ONLY_COMMAND = PineappleChat.component("<red>Only players can send this command!")!!
        val TOTEM_GIVE_USAGE = PineappleChat.component("<gray>/totem give <gold>\\<totem-type></gold>")!!
        val TOTEM_TYPE_NOT_EXISTS = PineappleChat.component("<red>The Totem <\$totem-type> does not exists")!!
        val TOTEM_GIVE_SUCCESS = PineappleChat.component("<green>given totem <\$totem-type> to <\$player-name>")!!
    }

}
