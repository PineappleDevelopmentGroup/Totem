package sh.miles.totem.util.serialized

import org.bukkit.damage.DamageType
import org.bukkit.potion.PotionEffect
import sh.miles.pineapple.PineappleLib
import sh.miles.pineapple.util.serialization.SerializedDeserializeContext
import sh.miles.pineapple.util.serialization.SerializedElement
import sh.miles.pineapple.util.serialization.SerializedObject
import sh.miles.pineapple.util.serialization.SerializedSerializeContext
import sh.miles.pineapple.util.serialization.adapter.SerializedAdapter
import sh.miles.totem.api.TotemSettings
import sh.miles.totem.api.impl.TotemSettingsImpl
import sh.miles.totem.registry.TotemSettingsRegistry

object TotemSettingsAdapter : SerializedAdapter<TotemSettings> {

    private val baseAnomaly = PineappleLib.getAnomalyFactory().create()
        .noThrowLog()

    override fun serialize(settings: TotemSettings, context: SerializedSerializeContext): SerializedElement {
        error("Not yet implemented")
    }

    override fun deserialize(element: SerializedElement, context: SerializedDeserializeContext): TotemSettings {
        if (element.isPrimitive) {
            return TotemSettingsRegistry.get(element.asPrimitive.asString).orThrow()
        }

        val parent = element.asObject;
        val id = baseAnomaly.run { parent.getOrNull("id")!!.asPrimitive.asString }
            .message("no ID path found when parsing settings entry at unknown instead found fields ${parent.entrySet()}")
            .hard(javaClass, "deserialize")
            .orThrow()

        val damageTypes = baseAnomaly.run {
            val list = mutableListOf<DamageType>();
            val damageArray = parent.getArray("damage-types").orThrow()
            for (damageElement in damageArray) {
                list.add(context.deserialize(damageElement, DamageType::class.java))
            }
            return@run list
        }.soft(javaClass, "deserialize").orElse(mutableListOf())

        val givenEffects = baseAnomaly.run {
            val list = mutableListOf<PotionEffect>()
            val effectArray = parent.getArray("potion-effects").orThrow()
            for (effectElement in effectArray) {
                list.add(context.deserialize(effectElement, PotionEffect::class.java))
            }
            return@run list
        }.soft(javaClass, "deserialize").orElse(mutableListOf())

        val standardEffects =
            if (parent.has("standard-effects")) parent.get("standard-effects").orThrow().asPrimitive.asBoolean else true
        val playParticles =
            if (parent.has("play-particles")) parent.get("play-particles").orThrow().asPrimitive.asBoolean else true
        return TotemSettingsImpl(
            id, damageTypes.toSet(), givenEffects.toSet(), standardEffects, playParticles
        )
    }

    override fun getKey(): Class<*> {
        return TotemSettings::class.java
    }

    private fun <T> deserializeOrDefault(
        parent: SerializedObject,
        field: String,
        context: SerializedDeserializeContext,
        function: (SerializedDeserializeContext) -> T,
        emptyT: () -> T
    ): T {
        if (parent.has(field)) {
            return function.invoke(context)
        }
        return emptyT.invoke()
    }
}
