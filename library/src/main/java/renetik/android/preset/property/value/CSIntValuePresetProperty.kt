package renetik.android.preset.property.value

import renetik.android.event.common.CSHasRegistrationsHasDestroy
import renetik.android.preset.CSPreset
import renetik.android.store.CSStore

class CSIntValuePresetProperty(
    parent: CSHasRegistrationsHasDestroy,
    preset: CSPreset<*, *>,
    key: String,
    override val default: Int,
    onChange: ((value: Int) -> Unit)?)
    : CSValuePresetProperty<Int>(parent,preset, key, onChange) {
    override fun get(store: CSStore) = store.getInt(key)
    override fun set(store: CSStore, value: Int) = store.set(key, value)
}


