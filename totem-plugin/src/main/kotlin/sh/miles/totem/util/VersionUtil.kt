package sh.miles.totem.util

import org.bukkit.Bukkit
import org.bukkit.Server

object VersionUtil {

    private val SERVER: Server = Bukkit.getServer()

    fun getOnlineMode(): String {
        if (SERVER.onlineMode) return "Online"
        return if (SERVER.spigot().config.getBoolean("settings.bungeecord", false)) "Bungee" else "Offline"
    }
}