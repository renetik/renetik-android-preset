package renetik.android.preset.property.nullable

import renetik.android.core.kotlin.className
import renetik.android.core.kotlin.collections.put
import renetik.android.event.CSEvent
import renetik.android.event.CSEvent.Companion.event
import renetik.android.event.invoke
import renetik.android.preset.CSPresetItem
import renetik.android.preset.model.CSPresetTestItem

class CSPresetTestPresetItemList : renetik.android.preset.CSPresetItemList<CSPresetItem> {
    override val id = className
    override val defaultItems = mutableListOf<CSPresetItem>()
    override val userItems = mutableListOf<CSPresetItem>()
    override val eventReload: CSEvent<Unit> = event()

    override fun remove(item: CSPresetItem) {
        defaultItems.remove(item)
    }

    override fun createItem(title: String, isDefault: Boolean): CSPresetItem {
        val items = if (isDefault) defaultItems else userItems
        return items.put(CSPresetTestItem(title))
    }

    override fun reload() = eventReload()
}