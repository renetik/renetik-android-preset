package renetik.android.preset.property.value

import renetik.android.event.common.CSHasRegistrationsHasDestroy
import renetik.android.preset.CSPreset
import renetik.android.store.CSStore

class CSStringValuePresetProperty(
    parent: CSHasRegistrationsHasDestroy,
    preset: CSPreset<*, *>,
    key: String,
    override val default: String,
    onChange: ((value: String) -> Unit)?)
    : CSValuePresetProperty<String>(parent, preset, key, onChange) {
    override fun get(store: CSStore) = store.getString(key)
    override fun set(store: CSStore, value: String) = store.set(key, value)
}

