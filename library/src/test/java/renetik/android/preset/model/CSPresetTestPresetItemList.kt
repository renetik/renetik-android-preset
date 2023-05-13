package renetik.android.preset.model

import renetik.android.core.kotlin.collections.put
import renetik.android.preset.CSPresetDataList
import renetik.android.preset.CSPresetItem
import renetik.android.preset.items

class CSPresetTestPresetItemList : CSPresetDataList<CSPresetItem> {
    override val defaultItems = mutableListOf<CSPresetItem>()
    override val userItems = mutableListOf<CSPresetItem>()

    override fun remove(item: CSPresetItem) {
        items.remove(item)
    }

    override fun createItem(title: String, isDefault: Boolean): CSPresetItem =
        (if (isDefault) defaultItems else userItems).put(CSPresetTestItem(title))

    override fun reload() = Unit
}