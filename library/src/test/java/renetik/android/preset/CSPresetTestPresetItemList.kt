package renetik.android.preset

import renetik.android.core.kotlin.collections.put

class CSPresetTestPresetItemList :
    CSPresetItemList<CSPresetTestPresetItem> {
    override val defaultItems = mutableListOf<CSPresetTestPresetItem>()
    override val userItems = mutableListOf<CSPresetTestPresetItem>()

    override fun remove(item: CSPresetTestPresetItem) {
        items.remove(item)
    }

    override fun createItem(
        title: String, isDefault: Boolean, id: String): CSPresetTestPresetItem {
        val items = if (isDefault) defaultItems else userItems
        return items.put(CSPresetTestPresetItem(items.size, title))
    }

    override fun reload() = Unit
}