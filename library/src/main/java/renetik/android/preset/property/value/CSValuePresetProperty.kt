package renetik.android.preset.property.value

import renetik.android.event.common.CSHasRegistrationsHasDestroy
import renetik.android.preset.CSPreset
import renetik.android.preset.property.CSPresetPropertyBase
import renetik.android.preset.property.CSPresetKeyData
import renetik.android.preset.property.store
import renetik.android.store.CSStore

abstract class CSValuePresetProperty<T>(
    parent: CSHasRegistrationsHasDestroy,
    preset: CSPreset<*, *>,
    override val key: String,
    onChange: ((value: T) -> Unit)? = null
) : CSPresetPropertyBase<T>(parent, preset, key, onChange), CSPresetKeyData {

    override fun loadFrom(store: CSStore): T = get(store) ?: default

    override fun load(): T = get(store) ?: default.also { value ->
        store.eventChanged.pause().use { set(store, value) }
    }
}