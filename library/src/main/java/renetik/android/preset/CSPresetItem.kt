package renetik.android.preset

import renetik.android.core.lang.CSHasId
import renetik.android.core.lang.CSHasIndex
import renetik.android.preset.property.CSPresetKeyData
import renetik.android.store.CSStore

interface CSPresetItem : CSHasId, CSHasIndex {
    companion object

    val store: CSStore
    fun save(properties: Iterable<CSPresetKeyData>)
}