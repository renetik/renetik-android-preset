package renetik.android.preset.property.value

import renetik.android.event.common.CSHasRegistrationsHasDestroy
import renetik.android.preset.CSPreset
import renetik.android.store.CSStore

class CSDoubleValuePresetProperty(
    parent: CSHasRegistrationsHasDestroy,
    preset: CSPreset<*, *>,
    key: String,
    override val default: Double,
    onChange: ((value: Double) -> Unit)?)
    : CSValuePresetProperty<Double>(parent,preset, key, onChange) {
    override fun get(store: CSStore) = store.getDouble(key)
    override fun set(store: CSStore, value: Double) = store.set(key, value)
}