package sh.miles.totem.registry

import org.bukkit.NamespacedKey
import sh.miles.pineapple.collection.registry.FrozenRegistry
import sh.miles.totem.TotemPlugin
import sh.miles.totem.totem.TotemSettings
import java.util.Arrays
import java.util.function.Supplier
import java.util.stream.Collectors

private const val FILE_NAME: String = "totem-settings.json"

object TotemSettingsRegistry : FrozenRegistry<TotemSettings, NamespacedKey>(Supplier {
    return@Supplier Arrays.stream(
        TotemPlugin.plugin.jhelper.asArray(
            TotemPlugin.plugin, FILE_NAME, Array<TotemSettings>::class.java
        )
    ).collect(Collectors.toMap({ it.key }, { it }))
}) {
    const val TOTEM_SETTINGS_NAME = FILE_NAME
}
