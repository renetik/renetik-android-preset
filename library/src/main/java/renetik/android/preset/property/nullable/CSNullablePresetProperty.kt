package renetik.android.preset.property.nullable

import renetik.android.event.registrations.CSHasRegistrationsHasDestroy
import renetik.android.preset.CSPreset
import renetik.android.preset.property.CSPresetPropertyBase
import renetik.android.preset.property.CSPresetKeyData
import renetik.android.preset.property.store
import renetik.android.store.CSStore

abstract class CSNullablePresetProperty<T>(
    parent: CSHasRegistrationsHasDestroy,
    preset: CSPreset<*, *>,
    override val key: String,
    onChange: ((value: T?) -> Unit)?)
    : CSPresetPropertyBase<T?>(parent, preset, key, onChange), CSPresetKeyData {

    override fun loadFrom(store: CSStore): T? =
        if (store.has(key)) get(store) else default

    override fun load(): T? =
        if (store.has(key)) get(store) else default?.also { value ->
            store.eventChanged.pause().use { set(store, value) }
        }
}