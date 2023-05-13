package renetik.android.preset

import renetik.android.core.kotlin.collections.put

class CSPresetTestPresetItemList :
    CSPresetDataList<CSPresetItem> {
    override val defaultItems = mutableListOf<CSPresetItem>()
    override val userItems = mutableListOf<CSPresetItem>()

    override fun remove(item: CSPresetItem) {
        items.remove(item)
    }

    override fun createItem(
        title: String, isDefault: Boolean
    ): CSPresetItem {
        val items = if (isDefault) defaultItems else userItems
        return items.put(CSPresetTestItem(title))
    }

    override fun reload() = Unit
}