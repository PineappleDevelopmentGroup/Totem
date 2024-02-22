package sh.miles.totem

import org.bukkit.plugin.java.JavaPlugin
import sh.miles.pineapple.PineappleLib
import sh.miles.pineapple.command.CommandRegistry
import sh.miles.pineapple.json.JsonHelper
import sh.miles.totem.command.TotemCommand
import sh.miles.totem.json.TotemSettingsAdapter
import sh.miles.totem.json.TotemTypeAdapter
import sh.miles.totem.listener.EntityDamageListener
import sh.miles.totem.registry.TotemSettingsRegistry
import sh.miles.totem.registry.TotemTypeRegistry
import java.io.File

class TotemPlugin : JavaPlugin() {
    companion object {
        lateinit var plugin: TotemPlugin
    }

    val jhelper = JsonHelper(
        TotemSettingsAdapter,
        TotemTypeAdapter
    )

    override fun onEnable() {
        plugin = this
        PineappleLib.initialize(this, true)
        saveResources()
        PineappleLib.getConfigurationManager().createStaticReloadable(File(dataFolder, "config.yml"), TotemConfig::class.java)

        val commands = CommandRegistry(this)
        commands.register(TotemCommand())

        TotemSettingsRegistry.run { }
        TotemTypeRegistry.run { }

        server.pluginManager.registerEvents(EntityDamageListener(), this)
    }

    override fun onDisable() {
        PineappleLib.cleanup()
    }

    private fun saveResources() {
        saveResource(TotemSettingsRegistry.TOTEM_SETTINGS_NAME, false)
        saveResource(TotemTypeRegistry.TOTEM_TYPES_FILE_NAME, false)
    }
}
