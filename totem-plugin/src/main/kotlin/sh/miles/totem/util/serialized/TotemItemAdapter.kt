package sh.miles.totem.util.serialized

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonElement
import com.google.gson.JsonSerializationContext
import org.bukkit.inventory.ItemStack
import sh.miles.pineapple.PineappleLib
import sh.miles.pineapple.item.ItemSpec
import sh.miles.pineapple.util.serialization.SerializedDeserializeContext
import sh.miles.pineapple.util.serialization.SerializedElement
import sh.miles.pineapple.util.serialization.SerializedSerializeContext
import sh.miles.pineapple.util.serialization.adapter.SerializedAdapter
import sh.miles.totem.api.TotemItem
import sh.miles.totem.api.TotemSettings
import sh.miles.totem.api.impl.TotemItemImpl
import java.lang.reflect.Type

object TotemItemAdapter : SerializedAdapter<TotemItem> {

    private val base = PineappleLib.getAnomalyFactory().create()

    override fun serialize(totemType: TotemItem, context: SerializedSerializeContext): SerializedElement {
        error("Not yet implemented")
    }

    override fun deserialize(element: SerializedElement, context: SerializedDeserializeContext): TotemItem {
        val parent = element.asObject
        val id = base
            .message("The given element does not have an ID field. All TotemItems must have an ID\n $element")
            .run { parent.get("id").orThrow().asPrimitive.asString }
            .hard(javaClass, "deserialize").orThrow()
        val settings = base
            .message("The given element does not have valid settings. All TotemItems must have valid settings")
            .run { context.deserialize(parent.get("settings").orThrow(), TotemSettings::class.java) }
            .hard(javaClass, "deserialize").orThrow()
        val item = base
            .message("The given element does not have a valid ItemStack. All TotemItems must have a valid ItemStack")
            .run { context.deserialize(parent.get("item").orThrow(), ItemSpec::class.java) }
            .hard(javaClass, "deserialize").orThrow()

        return TotemItemImpl(id, item.buildSpec(), settings)
    }

    override fun getKey(): Class<*> {
        return TotemItem::class.java
    }

}
