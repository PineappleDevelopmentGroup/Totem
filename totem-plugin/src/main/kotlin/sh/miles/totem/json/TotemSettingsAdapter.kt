package sh.miles.totem.json

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import org.bukkit.damage.DamageType
import org.bukkit.potion.PotionEffect
import sh.miles.pineapple.json.JsonAdapter
import sh.miles.totem.api.TotemSettings
import sh.miles.totem.api.impl.TotemSettingsImpl
import sh.miles.totem.registry.TotemSettingsRegistry
import java.lang.reflect.Type

object TotemSettingsAdapter : JsonAdapter<TotemSettings> {

    override fun serialize(settings: TotemSettings, type: Type, context: JsonSerializationContext): JsonElement {
        error("Not yet implemented")
    }

    override fun deserialize(element: JsonElement, type: Type, context: JsonDeserializationContext): TotemSettings {
        if (element is JsonPrimitive) {
            return TotemSettingsRegistry.get(element.asString).orElseThrow()
        }

        val parent = element.asJsonObject;
        verify(parent, "id", null)
        val id = parent.get("id").asString
        verify(parent, "damage-types", id)
        val damageTypes =
            context.deserialize<Array<DamageType>>(parent.get("damage-types"), Array<DamageType>::class.java)
        val givenEffects = deserializeOrDefault(parent, "given-effects", context, {
            return@deserializeOrDefault it.deserialize<Array<PotionEffect>>(
                parent.get("given-effects"), Array<PotionEffect>::class.java
            )
        }, {
            return@deserializeOrDefault arrayOf()
        })
        val standardEffects = if (parent.has("standard-effects")) parent.get("standard-effects").asBoolean else true
        val playParticles = if (parent.has("play-particles")) parent.get("play-particles").asBoolean else true
        return TotemSettingsImpl(
            id, damageTypes.toSet(), givenEffects.toSet(), standardEffects, playParticles
        )
    }

    override fun getAdapterType(): Class<TotemSettings> {
        return TotemSettings::class.java
    }

    private fun <T> deserializeOrDefault(
        parent: JsonObject,
        field: String,
        context: JsonDeserializationContext,
        function: (JsonDeserializationContext) -> T,
        emptyT: () -> T
    ): T {
        if (parent.has(field)) {
            return function.invoke(context)
        }
        return emptyT.invoke()
    }

    private fun verify(parent: JsonObject, path: String, id: String?) {
        if (!parent.has(path)) {
            error("no such path $path exists when parsing parent object, error occurred at ${id ?: "unknown"} instead found fields ${parent.asMap().keys}")
        }
    }
}
