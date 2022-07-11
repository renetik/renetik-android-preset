package renetik.android.preset.property.value

import renetik.android.event.common.CSHasRegistrationsHasDestroy
import renetik.android.store.type.CSJsonObjectStore
import renetik.android.preset.CSPreset
import renetik.android.store.CSStore
import renetik.android.core.kotlin.reflect.createInstance
import kotlin.reflect.KClass

open class CSJsonTypeValuePresetProperty<T : CSJsonObjectStore>(
    parent: CSHasRegistrationsHasDestroy,
    preset: CSPreset<*, *>, key: String, val type: KClass<T>,
    onChange: ((value: T) -> Unit)? = null)
    : CSValuePresetProperty<T>(parent, preset, key, onChange) {
    override val default get() = type.createInstance()!!
    override var _value = load()
    override fun get(store: CSStore) = store.getJsonObject(key, type)
    override fun set(store: CSStore, value: T) = store.set(key, value)
}


