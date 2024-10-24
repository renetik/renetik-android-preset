package renetik.android.preset.model

import renetik.android.core.kotlin.collections.put
import renetik.android.preset.CSPresetItemList
import renetik.android.preset.CSPresetItem

class CSPresetTestPresetItemList(clearItemId: String) : CSPresetItemList<CSPresetItem> {
    override val defaultItems = mutableListOf<CSPresetItem>()
    override val userItems = mutableListOf<CSPresetItem>()

    init {
        createItem(clearItemId, isDefault = true)
    }

    override fun remove(item: CSPresetItem) {
        defaultItems.remove(item)
        userItems.remove(item)
    }

    override fun createItem(title: String, isDefault: Boolean): CSPresetItem =
        (if (isDefault) defaultItems else userItems).put(CSPresetTestItem(id = title))

    override fun reload() = Unit
}