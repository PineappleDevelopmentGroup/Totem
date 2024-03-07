package sh.miles.totem.json

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonSerializationContext
import org.bukkit.inventory.ItemStack
import sh.miles.pineapple.PineappleLib
import sh.miles.pineapple.json.JsonAdapter
import sh.miles.totem.api.TotemItem
import sh.miles.totem.api.TotemSettings
import sh.miles.totem.api.impl.TotemItemImpl
import java.lang.reflect.Type

object TotemItemAdapter : JsonAdapter<TotemItem> {

    private val base = PineappleLib.getAnomalyFactory().create()
        .noThrowLog()

    override fun serialize(totemType: TotemItem, type: Type, context: JsonSerializationContext): JsonElement {
        error("Not yet implemented")
    }

    override fun deserialize(element: JsonElement, type: Type, context: JsonDeserializationContext): TotemItem {
        val parent = element.asJsonObject
        val id = base
            .message("The given element does not have an ID field. All TotemItems must have an ID\n $element")
            .run { parent.get("id").asString }
            .hard(javaClass, "deserialize").orThrow()
        val settings = base
            .message("The given element does not have valid settings. All TotemItems must have valid settings")
            .run { context.deserialize<TotemSettings>(parent.get("settings"), TotemSettings::class.java) }
            .hard(javaClass, "deserialize").orThrow()
        val item = base
            .message("The given element does not have a valid ItemStack. All TotemItems must have a valid ItemStack")
            .run { context.deserialize<ItemStack>(parent.get("item"), ItemStack::class.java) }
            .hard(javaClass, "deserialize").orThrow()

        return TotemItemImpl(id, item, settings)
    }

    override fun getAdapterType(): Class<TotemItem> {
        return TotemItem::class.java
    }
}
