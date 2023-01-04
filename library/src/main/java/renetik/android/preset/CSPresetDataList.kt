package renetik.android.preset

import java.lang.System.nanoTime

interface CSPresetDataList<PresetItem : CSPresetItem> {
    companion object

    val defaultItems: List<PresetItem>
    val userItems: List<PresetItem>
    fun remove(item: PresetItem)

    fun createItem(title: String, isDefault: Boolean,
                   id: String = "${nanoTime()}"): PresetItem

    fun reload()
}