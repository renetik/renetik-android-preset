package renetik.android.preset.model

import renetik.android.core.kotlin.className
import renetik.android.core.kotlin.collections.put
import renetik.android.preset.CSPresetItem
import renetik.android.preset.CSPresetItemList

class TestCSPresetItemList(firstItemId: String) : CSPresetItemList<CSPresetItem> {
    override val id = className
    override val defaultItems = mutableListOf<CSPresetItem>()
    override val userItems = mutableListOf<CSPresetItem>()

    init {
        createItem(firstItemId, isDefault = true)
    }

    override fun remove(item: CSPresetItem) {
        defaultItems.remove(item)
        userItems.remove(item)
    }

    override fun createItem(title: String, isDefault: Boolean): CSPresetItem =
        (if (isDefault) defaultItems else userItems).put(CSPresetTestItem(id = title))

    override fun reload() = Unit
}