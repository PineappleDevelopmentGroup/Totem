package sh.miles.totem.json

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonSerializationContext
import org.bukkit.inventory.ItemStack
import sh.miles.pineapple.json.JsonAdapter
import sh.miles.totem.api.TotemItem
import sh.miles.totem.api.TotemSettings
import sh.miles.totem.api.impl.TotemItemImpl
import java.lang.reflect.Type

object TotemItemAdapter : JsonAdapter<TotemItem> {
    override fun serialize(totemType: TotemItem, type: Type, context: JsonSerializationContext): JsonElement {
        error("Not yet implemented")
    }

    override fun deserialize(element: JsonElement, type: Type, context: JsonDeserializationContext): TotemItem {
        val parent = element.asJsonObject
        verify(parent, "id", null)
        val id = parent.get("id").asString
        verify(parent, "settings", id)
        val settings = context.deserialize<TotemSettings>(parent.get("settings"), TotemSettings::class.java)
        verify(parent, "item", id)
        val item = context.deserialize<ItemStack>(parent.get("item"), ItemStack::class.java)
        return TotemItemImpl(id, item, settings)
    }

    override fun getAdapterType(): Class<TotemItem> {
        return TotemItem::class.java
    }

    private fun verify(parent: JsonObject, path: String, id: String?) {
        if (!parent.has(path)) {
            error("no such path $path exists when parsing parent object, error occurred at ${id ?: "unknown"} instead found fields ${parent.asMap().keys}")
        }
    }
}
