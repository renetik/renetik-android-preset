package renetik.android.preset.property.nullable

import renetik.android.event.common.CSHasRegistrationsHasDestruct
import renetik.android.event.paused
import renetik.android.preset.CSPreset
import renetik.android.preset.property.CSPresetKeyData
import renetik.android.preset.property.CSPresetPropertyBase

abstract class CSNullablePresetProperty<T>(
    parent: CSHasRegistrationsHasDestruct,
    preset: CSPreset<*, *>,
    override val key: String,
    onChange: ((value: T?) -> Unit)?
) : CSPresetPropertyBase<T?>(parent, preset, key, onChange), CSPresetKeyData {

    var isOnLoadStoreDefaultValue = false

    override fun load(): T? =
        if (store.has(key)) getFiltered(store) else default?.also {
            if (isOnLoadStoreDefaultValue) storeDefault(it)
        }

    private fun storeDefault(value: T?) {
        store.eventChanged.paused { set(store, value) }
    }

    fun storeDefaultNow() = apply { storeDefault(default) }
}