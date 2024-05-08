package sh.miles.totem

import org.bstats.bukkit.Metrics
import org.bstats.charts.SimplePie
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import sh.miles.pineapple.PineappleLib
import sh.miles.pineapple.json.JsonHelper
import sh.miles.pineapple.updater.SimpleSemVersion
import sh.miles.pineapple.updater.UpdateChecker
import sh.miles.pineapple.util.serialization.Serialized
import sh.miles.pineapple.util.serialization.adapter.SerializedAdapter
import sh.miles.pineapple.util.serialization.adapter.SerializedAdapterRegistry
import sh.miles.pineapple.util.serialization.bridges.gson.GsonSerializedBridge
import sh.miles.totem.api.TotemApi
import sh.miles.totem.api.impl.TotemApiImpl
import sh.miles.totem.listener.EntityDamageListener
import sh.miles.totem.registry.TotemItemRegistry
import sh.miles.totem.registry.TotemRecipeRegistry
import sh.miles.totem.registry.TotemSettingsRegistry
import sh.miles.totem.ui.command.TotemCommand
import sh.miles.totem.util.VersionUtil
import sh.miles.totem.util.serialized.TotemItemAdapter
import sh.miles.totem.util.serialized.TotemSettingsAdapter
import sh.miles.totem.util.serialized.recipe.TotemRecipeAdapter
import java.io.File

class TotemPlugin : JavaPlugin() {
    companion object {
        lateinit var plugin: TotemPlugin
    }

    lateinit var jsonHelper: JsonHelper

    override fun onEnable() {
        plugin = this
        PineappleLib.initialize(this)
        this.jsonHelper = JsonHelper { builder ->
            SerializedAdapterRegistry.INSTANCE.register(TotemItemAdapter)
            SerializedAdapterRegistry.INSTANCE.register(TotemRecipeAdapter)
            SerializedAdapterRegistry.INSTANCE.register(TotemSettingsAdapter)
            SerializedAdapterRegistry.INSTANCE.registerBridge(GsonSerializedBridge(builder))
        }
        PineappleLib.getCommandRegistry().registerInternalCommands()
        TotemApi.setApiInstance(TotemApiImpl())

        PineappleLib.getConfigurationManager()
            .createDefault(File(plugin.dataFolder, "config.yml"), TotemConfig::class.java)
        saveResources()

        TotemSettingsRegistry.run { }
        TotemItemRegistry.run { }
        TotemRecipeRegistry.run {
            for (key in keys()) {
                Bukkit.addRecipe(get(key).orThrow().recipe)
            }
        }

        server.pluginManager.registerEvents(EntityDamageListener(), this)

        PineappleLib.getCommandRegistry().register(TotemCommand())

        val metrics = Metrics(this, 21203)
        metrics.addCustomChart(SimplePie("online_mode", VersionUtil::getOnlineMode))

        UpdateChecker(this, 115382).getVersion { spigotVersion ->
            if (SimpleSemVersion.fromString(spigotVersion)
                    .isNewerThan(SimpleSemVersion.fromString(this.description.version))
            ) {
                val logger = logger
                logger.info("Your version of totem is out of date, please download the latest for new features and bug fixes")
                logger.info("Download here: https://www.spigotmc.org/resources/totem.115382/")
            }
        }
    }

    override fun onDisable() {
        PineappleLib.cleanup()
    }

    private fun saveResources() {
        saveResource(TotemSettingsRegistry.TOTEM_SETTINGS_NAME, false)
        saveResource(TotemItemRegistry.TOTEM_ITEMS_NAME, false)
        saveResource(TotemRecipeRegistry.TOTEM_RECIPES_NAME, false)
    }
}
