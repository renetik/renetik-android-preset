package renetik.android.preset

class CSPresetTestPresetItemList :
    CSPresetItemList<CSPresetTestPresetItem> {
    override val defaultItems = mutableListOf<CSPresetTestPresetItem>()
    override val userItems = mutableListOf<CSPresetTestPresetItem>()

    override fun add(item: CSPresetTestPresetItem) {
        defaultItems.add(item)
    }

    override fun remove(item: CSPresetTestPresetItem) {
        items.remove(item)
    }

    override fun createPresetItem(title: String, isDefault: Boolean, id: String) =
        CSPresetTestPresetItem(title)

    override fun reload() = Unit
}