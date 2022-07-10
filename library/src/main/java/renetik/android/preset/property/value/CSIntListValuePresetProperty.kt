package renetik.android.preset.property.value

import renetik.android.event.registrations.CSHasRegistrationsHasDestroy
import renetik.android.preset.CSPreset
import renetik.android.store.CSStore

class CSIntListValuePresetProperty(
    parent: CSHasRegistrationsHasDestroy,
    preset: CSPreset<*, *>, key: String,
    override val default: List<Int>,
    onChange: ((value: List<Int>) -> Unit)? = null)
    : CSValuePresetProperty<List<Int>>(parent, preset, key, onChange) {

    override var _value = load()

    override fun get(store: CSStore) = store.get(key)?.split(",")
        ?.map { it.toInt() } ?: default

    override fun set(store: CSStore, value: List<Int>) =
        store.set(key, value.joinToString(",") { it.toString() })
}