package sh.miles.totem

import sh.miles.pineapple.chat.PineappleChat
import sh.miles.pineapple.config.annotation.ConfigPath

object TotemConfig {

    @JvmStatic
    @ConfigPath("player-only-command")
    var PLAYER_ONLY_COMMAND = PineappleChat.component("<red>Only players can send this command!")!!

    @JvmStatic
    @ConfigPath("totem-give-usage")
    var TOTEM_GIVE_USAGE = PineappleChat.component("<gray>/totem give <gold>\\<totem-type></gold>")!!

    @JvmStatic
    @ConfigPath("totem-type-not-exists")
    var TOTEM_TYPE_NOT_EXISTS = PineappleChat.component("<red>The Totem <\$totem-type> does not exists")!!

    @JvmStatic
    @ConfigPath("totem-give-success")
    var TOTEM_GIVE_SUCCESS = PineappleChat.component("<green>given totem <\$totem-type> to <\$player-name>")!!
}
