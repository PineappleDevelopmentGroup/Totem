package sh.miles.totem.registry

import sh.miles.pineapple.collection.registry.WriteableRegistry
import sh.miles.totem.api.TotemSettings
import sh.miles.totem.TotemPlugin
import java.util.Arrays
import java.util.function.Supplier
import java.util.stream.Collectors

private const val FILE_NAME: String = "totem-settings.json"

object TotemSettingsRegistry : WriteableRegistry<TotemSettings, String>(Supplier {
    return@Supplier Arrays.stream(
        TotemPlugin.plugin.jsonHelper.asArray(
            TotemPlugin.plugin, FILE_NAME, Array<TotemSettings>::class.java
        )
    ).collect(Collectors.toMap({ it.key }, { it }))
}) {
    const val TOTEM_SETTINGS_NAME = FILE_NAME
}
