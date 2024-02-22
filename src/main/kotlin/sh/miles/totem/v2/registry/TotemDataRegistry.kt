package sh.miles.totem.v2.registry

import sh.miles.pineapple.collection.registry.WriteableRegistry
import sh.miles.totem.TotemPlugin
import sh.miles.totem.v2.totem.Totem
import sh.miles.totem.v2.totem.TotemData
import java.util.Arrays
import java.util.function.Supplier
import java.util.stream.Collectors

object TotemDataRegistry : WriteableRegistry<TotemData, String>(Supplier {
    return@Supplier Arrays.stream(
        TotemPlugin.plugin.jhelper.asArray(
            TotemPlugin.plugin, Totem.TOTEM_DATA_REGISTRY_FILE_NAME, Array<TotemData>::class.java
        )
    ).collect(Collectors.toMap({ it.key }, { it }))
})