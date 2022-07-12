package renetik.android.preset.property

import renetik.android.preset.CSPreset
import renetik.android.event.common.CSHasDestroy
import renetik.android.store.CSStore

interface CSPresetKeyData : CSHasDestroy {
    companion object
    val preset: CSPreset<*, *>
    val key: String
    fun saveTo(store: CSStore)
}