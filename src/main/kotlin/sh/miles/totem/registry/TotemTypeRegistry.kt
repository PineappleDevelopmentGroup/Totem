package sh.miles.totem.registry

import org.bukkit.NamespacedKey
import sh.miles.pineapple.collection.registry.FrozenRegistry
import sh.miles.totem.TotemPlugin
import sh.miles.totem.totem.TotemType
import java.util.Arrays
import java.util.function.Supplier
import java.util.stream.Collectors

private const val FILE_NAME: String = "totem-types.json"

object TotemTypeRegistry : FrozenRegistry<TotemType, NamespacedKey>(Supplier {
    return@Supplier Arrays.stream(
        TotemPlugin.plugin.jhelper.asArray(
            TotemPlugin.plugin, FILE_NAME, Array<TotemType>::class.java
        )
    ).collect(Collectors.toMap({ it.key }, { it }))
}) {
    const val TOTEM_TYPES_FILE_NAME = FILE_NAME
}
