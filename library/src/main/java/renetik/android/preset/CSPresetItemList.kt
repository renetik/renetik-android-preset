package renetik.android.preset

import renetik.android.core.lang.CSHasId

interface CSPresetItemList<PresetItem : CSPresetItem> : CSHasId {
    companion object

    val defaultItems: List<PresetItem>
    val userItems: List<PresetItem>
    fun remove(item: PresetItem)
    fun createItem(title: String, isDefault: Boolean): PresetItem
    fun reload()
}