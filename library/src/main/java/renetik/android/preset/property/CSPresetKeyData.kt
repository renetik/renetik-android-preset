package renetik.android.preset.property

import renetik.android.event.common.CSHasDestruct
import renetik.android.preset.CSPreset
import renetik.android.store.CSStore

interface CSPresetKeyData : CSHasDestruct {
    companion object

    val preset: CSPreset<*, *>
    val key: String
    fun saveTo(store: CSStore)
    fun onStoreLoaded()
    fun isModifiedIn(store: CSStore): Boolean = false
}