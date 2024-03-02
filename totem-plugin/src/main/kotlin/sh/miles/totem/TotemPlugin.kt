package sh.miles.totem

import org.bstats.bukkit.Metrics
import org.bstats.charts.SimplePie
import org.bukkit.Bukkit
import org.bukkit.inventory.Recipe
import org.bukkit.plugin.java.JavaPlugin
import sh.miles.pineapple.PineappleLib
import sh.miles.pineapple.json.JsonHelper
import sh.miles.pineapple.updater.SimpleSemVersion
import sh.miles.pineapple.updater.UpdateChecker
import sh.miles.totem.api.TotemApi
import sh.miles.totem.api.impl.TotemApiImpl
import sh.miles.totem.command.TotemCommand
import sh.miles.totem.json.TotemItemAdapter
import sh.miles.totem.json.TotemRecipeAdapter
import sh.miles.totem.json.TotemSettingsAdapter
import sh.miles.totem.listener.EntityDamageListener
import sh.miles.totem.registry.TotemItemRegistry
import sh.miles.totem.registry.TotemSettingsRegistry
import sh.miles.totem.util.VersionUtil
import java.io.File

class TotemPlugin : JavaPlugin() {
    companion object {
        lateinit var plugin: TotemPlugin
    }

    val jsonHelper = JsonHelper(
        TotemSettingsAdapter,
        TotemItemAdapter,
        TotemRecipeAdapter
    )

    override fun onEnable() {
        plugin = this
        PineappleLib.initialize(this)
        PineappleLib.getCommandRegistry().registerInternalCommands()
        TotemApi.setApiInstance(TotemApiImpl())

        PineappleLib.getConfigurationManager()
            .createDefault(File(plugin.dataFolder, "config.yml"), TotemConfig::class.java)
        saveResources()

        TotemSettingsRegistry.run { }
        TotemItemRegistry.run { }
        jsonHelper.asArray(this, "totem-recipes.json", Array<Recipe>::class.java).forEach { Bukkit.addRecipe(it) }

        server.pluginManager.registerEvents(EntityDamageListener(), this)

        PineappleLib.getCommandRegistry().register(TotemCommand())

        val metrics = Metrics(this, 21203)
        metrics.addCustomChart(SimplePie("online_mode", VersionUtil::getOnlineMode))

        UpdateChecker(this, 115382).getVersion { spigotVersion ->
            if (SimpleSemVersion.fromString(spigotVersion).isNewerThan(SimpleSemVersion.fromString(this.description.version))) {
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
        saveResource("totem-recipes.json", false)
    }
}
