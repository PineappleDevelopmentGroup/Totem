package sh.miles.totem

import org.bukkit.plugin.java.JavaPlugin
import sh.miles.pineapple.PineappleLib
import sh.miles.pineapple.json.JsonHelper
import sh.miles.totem.api.TotemApi
import sh.miles.totem.api.impl.TotemApiImpl
import sh.miles.totem.command.TotemCommand
import sh.miles.totem.json.TotemItemAdapter
import sh.miles.totem.json.TotemSettingsAdapter
import sh.miles.totem.listener.EntityDamageListener
import sh.miles.totem.registry.TotemItemRegistry
import sh.miles.totem.registry.TotemSettingsRegistry
import java.io.File

class TotemPlugin : JavaPlugin() {
    companion object {
        lateinit var plugin: TotemPlugin
    }

    val jsonHelper = JsonHelper(
        TotemSettingsAdapter,
        TotemItemAdapter,
    )

    override fun onEnable() {
        plugin = this
        PineappleLib.initialize(this)
        PineappleLib.getCommandRegistry().registerInternalCommands()
        TotemApi.setApiInstance(TotemApiImpl())

        PineappleLib.getConfigurationManager().createDefault(File(plugin.dataFolder, "config.yml"), TotemConfig::class.java)
        saveResources()

        TotemSettingsRegistry.run { }
        TotemItemRegistry.run { }

        server.pluginManager.registerEvents(EntityDamageListener(), this)

        PineappleLib.getCommandRegistry().register(TotemCommand())
    }

    override fun onDisable() {
        PineappleLib.cleanup()
    }

    private fun saveResources() {
        saveResource(TotemSettingsRegistry.TOTEM_SETTINGS_NAME, false)
        saveResource(TotemItemRegistry.TOTEM_ITEMS_NAME, false)
    }
}
