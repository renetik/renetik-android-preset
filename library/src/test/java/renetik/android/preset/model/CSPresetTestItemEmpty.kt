package renetik.android.preset.model

import renetik.android.preset.CSPresetItem
import renetik.android.preset.CSPresetItem.Companion.NotFoundPresetItemId
import renetik.android.store.CSStore

class CSPresetTestItemEmpty(override val store: CSStore) : CSPresetItem {
    override val id: String = NotFoundPresetItemId
}