package renetik.android.preset

interface CSPresetItemList<PresetItem : CSPresetItem> {
    companion object

    val defaultItems: List<PresetItem>
    val userItems: List<PresetItem>
    fun remove(item: PresetItem)
    fun createItem(title: String, isDefault: Boolean): PresetItem
    fun reload()
}