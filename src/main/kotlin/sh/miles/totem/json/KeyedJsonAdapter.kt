package sh.miles.totem.json

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonElement
import com.google.gson.JsonSerializationContext
import org.bukkit.Keyed
import org.bukkit.NamespacedKey
import org.bukkit.Registry
import sh.miles.pineapple.json.JsonAdapter
import java.lang.reflect.Type

interface KeyedJsonAdapter<T : Keyed> : JsonAdapter<T> {

    override fun serialize(registryResult: T, type: Type, context: JsonSerializationContext): JsonElement {
        return context.serialize(registryResult.key, NamespacedKey::class.java)
    }

    override fun deserialize(element: JsonElement, type: Type, context: JsonDeserializationContext): T {
        return registry().get(context.deserialize(element, NamespacedKey::class.java))!!
    }

    fun registry(): Registry<T>
}
