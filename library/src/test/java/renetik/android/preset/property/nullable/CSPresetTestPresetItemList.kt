package renetik.android.preset.property.nullable

import renetik.android.core.kotlin.collections.put
import renetik.android.preset.CSPresetItem
import renetik.android.preset.model.CSPresetTestItem

class CSPresetTestPresetItemList : renetik.android.preset.CSPresetItemList<CSPresetItem> {
    override val defaultItems = mutableListOf<CSPresetItem>()
    override val userItems = mutableListOf<CSPresetItem>()

    override fun remove(item: CSPresetItem) {
        defaultItems.remove(item)
    }

    override fun createItem(title: String, isDefault: Boolean): CSPresetItem {
        val items = if (isDefault) defaultItems else userItems
        return items.put(CSPresetTestItem(title))
    }

    override fun reload() = Unit
}