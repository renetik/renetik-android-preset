package renetik.android.preset

import renetik.android.event.CSEvent

interface CSPresetItemList<PresetItem : CSPresetItem> {
    companion object

    val defaultItems: List<PresetItem>
    val userItems: List<PresetItem>
    fun remove(item: PresetItem)
    fun createItem(title: String, isDefault: Boolean): PresetItem
    val onReload: CSEvent<Unit>
    fun reload()
}