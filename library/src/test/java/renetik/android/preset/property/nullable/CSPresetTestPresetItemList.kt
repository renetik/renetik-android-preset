package renetik.android.preset.property.nullable

import renetik.android.core.kotlin.collections.put

class CSPresetTestPresetItemList : renetik.android.preset.CSPresetItemList<CSPresetTestPresetItem> {
    override val defaultItems = mutableListOf<CSPresetTestPresetItem>()
    override val userItems = mutableListOf<CSPresetTestPresetItem>()

    override fun remove(item: CSPresetTestPresetItem) {
        defaultItems.remove(item)
    }

    override fun createItem(title: String, isDefault: Boolean, id: String): CSPresetTestPresetItem {
        val items = if (isDefault) defaultItems else userItems
        return items.put(CSPresetTestPresetItem(items.size, title))
    }

    override fun reload() = Unit
}