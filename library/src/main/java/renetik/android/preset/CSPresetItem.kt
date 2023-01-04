package renetik.android.preset

import renetik.android.core.lang.CSHasId
import renetik.android.core.lang.CSHasIndex
import renetik.android.preset.property.CSPresetKeyData
import renetik.android.store.CSStore
import renetik.android.store.property.CSStoreProperty

interface CSPresetItem : CSHasId, CSHasIndex {
    companion object

    val store: CSStore

    val title: CSStoreProperty<String>

    fun save(properties: Iterable<CSPresetKeyData>)
}