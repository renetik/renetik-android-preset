package renetik.android.preset.property.nullable

class CSPresetTestPresetItemList : renetik.android.preset.CSPresetItemList<CSPresetTestPresetItem> {
	override val defaultItems = mutableListOf<CSPresetTestPresetItem>()
	override val userItems = mutableListOf<CSPresetTestPresetItem>()
	override fun add(item: CSPresetTestPresetItem) {
		defaultItems.add(item)
	}

	override fun remove(item: CSPresetTestPresetItem) {
		defaultItems.remove(item)
	}

	override fun createPresetItem(title: String, isDefault: Boolean, id: String) =
        CSPresetTestPresetItem(title)

	override fun reload() = Unit
}