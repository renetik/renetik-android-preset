package renetik.android.preset

import java.lang.System.nanoTime

interface CSPresetItemList<PresetItem : CSPresetItem> {
    companion object

    val defaultItems: List<PresetItem>
    val userItems: List<PresetItem>
    fun remove(item: PresetItem)
    fun createPresetItem(
        title: String, isDefault: Boolean,
        id: String = "${nanoTime()}"): PresetItem

    fun reload()
}