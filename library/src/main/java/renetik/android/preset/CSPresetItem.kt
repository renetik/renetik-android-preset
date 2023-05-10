package renetik.android.preset

import renetik.android.core.lang.CSHasId
import renetik.android.core.lang.CSHasIndex
import renetik.android.preset.property.CSPresetKeyData
import renetik.android.store.CSStore
import renetik.android.store.property.CSStoreProperty

interface CSPresetItem : CSHasId {
    companion object {
        const val NotFoundPresetItemId = "NotFoundPresetItem"
    }

    val store: CSStore

    fun save(properties: Iterable<CSPresetKeyData>)
}