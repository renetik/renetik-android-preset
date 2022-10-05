package renetik.android.preset.property.nullable

import renetik.android.core.lang.ArgFunc
import renetik.android.event.common.CSHasRegistrationsHasDestroy
import renetik.android.preset.Preset
import renetik.android.store.CSStore

class CSBooleanNullablePresetProperty(
    parent: CSHasRegistrationsHasDestroy,
    preset: Preset,
    key: String,
    override val default: Boolean?,
    onChange: ArgFunc<Boolean?>?)
    : CSNullablePresetProperty<Boolean>(parent, preset, key, onChange) {
    override fun get(store: CSStore): Boolean? = store.getBoolean(key)
    override fun set(store: CSStore, value: Boolean?) = store.set(key, value)
}


