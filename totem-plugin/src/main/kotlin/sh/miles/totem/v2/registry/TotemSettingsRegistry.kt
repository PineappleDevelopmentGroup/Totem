package sh.miles.totem.v2.registry

import sh.miles.pineapple.collection.registry.WriteableRegistry
import sh.miles.totem.TotemPlugin
import sh.miles.totem.v2.totem.Totem
import sh.miles.totem.v2.totem.TotemSettings
import java.util.Arrays
import java.util.function.Supplier
import java.util.stream.Collectors

object TotemSettingsRegistry : WriteableRegistry<TotemSettings, String>(Supplier {
    return@Supplier Arrays.stream(
        TotemPlugin.plugin.jhelper.asArray(
            TotemPlugin.plugin, Totem.TOTEM_SETTINGS_REGISTRY_FILE_NAME, Array<TotemSettings>::class.java
        )
    ).collect(Collectors.toMap({ it.key }, { it }))
})
