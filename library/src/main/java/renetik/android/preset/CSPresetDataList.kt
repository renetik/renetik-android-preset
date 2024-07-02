package renetik.android.preset

interface CSPresetDataList<PresetItem : CSPresetItem> {
    companion object

    val defaultItems: List<PresetItem>
    val userItems: List<PresetItem>
    fun remove(item: PresetItem)
    fun createItem(title: String, isDefault: Boolean): PresetItem
    fun reload()
}

val CSPresetDataList<*>.size: Int get() = defaultItems.size + userItems.size