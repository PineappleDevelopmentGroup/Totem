package sh.miles.totem.json

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonSerializationContext
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import sh.miles.pineapple.json.JsonAdapter
import java.lang.reflect.Type

internal object PotionEffectAdapter : JsonAdapter<PotionEffect> {

    override fun serialize(effect: PotionEffect, type: Type, context: JsonSerializationContext): JsonElement {
        val parent = JsonObject()
        parent.add("type", context.serialize(effect.type))
        parent.addProperty("duration", effect.duration)
        parent.addProperty("amplifier", effect.amplifier)
        parent.addProperty("ambient", effect.isAmbient)
        parent.addProperty("particles", effect.hasParticles())
        parent.addProperty("icon", effect.hasIcon())
        return parent
    }

    override fun deserialize(element: JsonElement, sillyType: Type, context: JsonDeserializationContext): PotionEffect {
        val parent = element.asJsonObject
        val type: PotionEffectType = context.deserialize(parent.get("type"), PotionEffectType::class.java)
        val duration = if (parent.has("duration")) parent.get("duration").asInt else 20
        val amplifier = if (parent.has("amplifier")) parent.get("amplifier").asInt else 1
        val ambient = if (parent.has("ambient")) parent.get("ambient").asBoolean else true
        val particles = if (parent.has("particles")) parent.get("particles").asBoolean else true
        val icon = if (parent.has("icon")) parent.get("icon").asBoolean else true

        return PotionEffect(type, duration, amplifier, ambient, particles, icon)
    }

    override fun getAdapterType(): Class<PotionEffect> {
        return PotionEffect::class.java
    }

}
