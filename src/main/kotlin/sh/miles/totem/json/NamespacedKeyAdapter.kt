package sh.miles.totem.json

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import org.bukkit.NamespacedKey
import sh.miles.pineapple.json.JsonAdapter
import java.lang.reflect.Type

object NamespacedKeyAdapter : JsonAdapter<NamespacedKey> {
    override fun serialize(key: NamespacedKey, type: Type, context: JsonSerializationContext): JsonElement {
        return JsonPrimitive(key.toString())
    }

    override fun deserialize(element: JsonElement, type: Type, context: JsonDeserializationContext): NamespacedKey {
        val stringied: String = element.asString
        if (!stringied.contains(":")) {
            return NamespacedKey.minecraft(stringied)
        }
        return NamespacedKey.fromString(stringied)!!
    }

    override fun getAdapterType(): Class<NamespacedKey> {
        return NamespacedKey::class.java
    }
}
