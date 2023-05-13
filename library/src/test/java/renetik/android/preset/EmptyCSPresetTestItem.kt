package renetik.android.preset

import renetik.android.preset.CSPresetItem.Companion.NotFoundPresetItemId
import renetik.android.store.CSStore

class EmptyCSPresetTestItem(override val store: CSStore) : CSPresetItem {
    override val id: String = NotFoundPresetItemId
}