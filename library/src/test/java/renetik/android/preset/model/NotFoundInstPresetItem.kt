package renetik.android.preset.model

import renetik.android.preset.CSPresetItem
import renetik.android.store.CSStore
import renetik.android.store.type.CSJsonObjectStore

class NotFoundInstPresetItem : CSPresetItem {
    override val store: CSStore = CSJsonObjectStore()
    override val id: String = CSPresetItem.NotFoundPresetItemId
}