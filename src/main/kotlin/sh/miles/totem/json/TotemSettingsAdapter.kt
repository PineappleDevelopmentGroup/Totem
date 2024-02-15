package sh.miles.totem.json

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import org.bukkit.NamespacedKey
import org.bukkit.Particle
import org.bukkit.block.data.type.Bed.Part
import org.bukkit.damage.DamageType
import org.bukkit.potion.PotionEffect
import sh.miles.pineapple.json.JsonAdapter
import sh.miles.totem.registry.TotemSettingsRegistry
import sh.miles.totem.totem.TotemSettings
import java.lang.reflect.Type
import java.util.Arrays
import javax.xml.stream.events.Namespace

object TotemSettingsAdapter : JsonAdapter<TotemSettings> {

    override fun serialize(settings: TotemSettings, type: Type, context: JsonSerializationContext): JsonElement {
        error("Not yet implemented")
    }

    override fun deserialize(element: JsonElement, type: Type, context: JsonDeserializationContext): TotemSettings {
        if (element is JsonPrimitive) {
            return TotemSettingsRegistry.get(context.deserialize(element, NamespacedKey::class.java)).orElseThrow()
        }

        val parent = element.asJsonObject;
        val id = context.deserialize<NamespacedKey>(parent.get("id"), NamespacedKey::class.java)
        val damageTypes =
            context.deserialize<Array<DamageType>>(parent.get("damage-types"), Array<DamageType>::class.java)
        val givenEffects =
            context.deserialize<Array<PotionEffect>>(parent.get("given-effects"), Array<PotionEffect>::class.java)
        val standardEffects = if (parent.has("standard-effects")) parent.get("standard-effects").asBoolean else true
        return TotemSettings(
            id,
            damageTypes.toList(),
            givenEffects.toList(),
            standardEffects
        )
    }

    override fun getAdapterType(): Class<TotemSettings> {
        return TotemSettings::class.java
    }
}
