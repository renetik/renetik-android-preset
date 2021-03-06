package renetik.android.preset.property.value

import renetik.android.event.common.CSHasRegistrationsHasDestroy
import renetik.android.store.type.CSJsonObjectStore
import renetik.android.preset.CSPreset
import renetik.android.store.CSStore
import kotlin.reflect.KClass

class CSJsonListValuePresetProperty<T : CSJsonObjectStore>(
    parent: CSHasRegistrationsHasDestroy,
    preset: CSPreset<*, *>,
    key: String,
    val type: KClass<T>,
    override val default: List<T> = emptyList(),
    onChange: ((value: List<T>) -> Unit)?)
    : CSValuePresetProperty<List<T>>(parent,preset, key, onChange) {
    override var _value = load()
    override fun get(store: CSStore) = store.getJsonObjectList(key, type) ?: default
    override fun set(store: CSStore, value: List<T>) = store.set(key, value)
}