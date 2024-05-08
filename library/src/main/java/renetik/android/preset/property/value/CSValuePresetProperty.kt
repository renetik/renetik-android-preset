package renetik.android.preset.property.value

import renetik.android.event.common.CSHasDestruct
import renetik.android.event.paused
import renetik.android.preset.CSPreset
import renetik.android.preset.property.CSPresetKeyData
import renetik.android.preset.property.CSPresetPropertyBase
import renetik.android.store.CSStore

abstract class CSValuePresetProperty<T>(
    parent: CSHasDestruct,
    preset: CSPreset<*, *>,
    override val key: String,
    onChange: ((value: T) -> Unit)? = null,
) : CSPresetPropertyBase<T>(parent, preset, key, onChange), CSPresetKeyData {

    var isOnLoadStoreDefaultValue = false

    override fun load(): T = getFiltered(store) ?: default.also {
        if (isOnLoadStoreDefaultValue) storeDefault(it)
    }

    private fun storeDefault(value: T) {
        if (!isStored) store.eventChanged.paused { set(store, value) }
    }

    fun storeDefaultNow() = apply { storeDefault(default) }
}
