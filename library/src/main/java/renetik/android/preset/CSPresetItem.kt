package renetik.android.preset

import renetik.android.core.lang.CSHasId
import renetik.android.preset.property.CSPresetKeyData
import renetik.android.store.CSStore

interface CSPresetItem : CSHasId {
    companion object {
        //TODO: move to instruments library project somewhere
        const val NotFoundPresetItemId = "NotFoundPresetItem"
    }

    val store: CSStore
    fun save(properties: Iterable<CSPresetKeyData>)
}