package sh.miles.totem.json

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonElement
import com.google.gson.JsonSerializationContext
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import sh.miles.pineapple.json.JsonAdapter
import sh.miles.totem.totem.TotemSettings
import sh.miles.totem.totem.TotemType
import java.lang.reflect.Type

object TotemTypeAdapter : JsonAdapter<TotemType> {
    override fun serialize(totemType: TotemType, type: Type, context: JsonSerializationContext): JsonElement {
        error("Not yet implemented")
    }

    override fun deserialize(element: JsonElement, type: Type, context: JsonDeserializationContext): TotemType {
        val parent = element.asJsonObject
        val id = context.deserialize<NamespacedKey>(parent.get("id"), NamespacedKey::class.java)
        val settings = context.deserialize<TotemSettings>(parent.get("settings"), TotemSettings::class.java)
        val item = context.deserialize<ItemStack>(parent.get("item"), ItemStack::class.java)
        return TotemType(id, settings, item)
    }

    override fun getAdapterType(): Class<TotemType> {
        return TotemType::class.java
    }
}
