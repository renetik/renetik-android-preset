package renetik.android.preset.model

import renetik.android.core.kotlin.collections.put
import renetik.android.preset.CSPresetDataList
import renetik.android.preset.CSPresetItem
import renetik.android.preset.items

class CSPresetTestPresetItemList(clearItemId: String) : CSPresetDataList<CSPresetItem> {
    override val defaultItems = mutableListOf<CSPresetItem>()
    override val userItems = mutableListOf<CSPresetItem>()

    init {
        createItem(clearItemId, isDefault = true)
    }

    override fun remove(item: CSPresetItem) {
        items.remove(item)
    }

    override fun createItem(title: String, isDefault: Boolean): CSPresetItem =
        (if (isDefault) defaultItems else userItems).put(CSPresetTestItem(id = title))

    override fun reload() = Unit
}