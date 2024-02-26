package sh.miles.totem.api.impl

import sh.miles.pineapple.collection.registry.WriteableRegistry
import sh.miles.totem.api.Totem
import sh.miles.totem.api.TotemItem
import sh.miles.totem.api.TotemSettings
import sh.miles.totem.registry.TotemItemRegistry
import sh.miles.totem.registry.TotemSettingsRegistry

class TotemApiImpl : Totem {
    override fun getTotemSettingsRegistry(): WriteableRegistry<TotemSettings, String> {
        return TotemSettingsRegistry
    }

    override fun getTotemItemRegistry(): WriteableRegistry<TotemItem, String> {
        return TotemItemRegistry
    }

    override fun buildTotemItem(): TotemItem.Builder {
        return TotemItemImpl.Builder()
    }

    override fun buildTotemSettings(): TotemSettings.Builder {
        return TotemSettingsImpl.Builder()
    }
}
