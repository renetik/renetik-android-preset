package renetik.android.preset.property.nullable

import renetik.android.event.common.CSHasRegistrationsHasDestroy
import renetik.android.preset.CSPreset
import renetik.android.store.CSStore

class CSStringNullablePresetProperty(
    parent: CSHasRegistrationsHasDestroy,
    preset: CSPreset<*, *>,
    key: String,
    override val default: String?,
    onChange: ((value: String?) -> Unit)?)
    : CSNullablePresetProperty<String>(parent, preset, key, onChange) {
    override fun get(store: CSStore): String? = store.getString(key)
    override fun set(store: CSStore, value: String?) {
        store.set(key, value)
    }
}