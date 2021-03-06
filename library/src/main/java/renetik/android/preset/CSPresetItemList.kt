package renetik.android.preset

import renetik.android.core.java.util.currentTime

interface CSPresetItemList<PresetItem : CSPresetItem> {
    companion object
    val defaultItems: List<PresetItem>
    val userItems: List<PresetItem>
    fun add(item: PresetItem)
    fun remove(item: PresetItem)
    fun createPresetItem(title: String, isDefault: Boolean,
                         id: String = "$currentTime"): PresetItem
    fun reload()
}