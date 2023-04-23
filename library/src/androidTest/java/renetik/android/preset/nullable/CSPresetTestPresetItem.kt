package renetik.android.preset.nullable

import renetik.android.preset.CSPresetItem
import renetik.android.preset.property.CSPresetKeyData
import renetik.android.store.extensions.property
import renetik.android.store.type.CSJsonObjectStore

class CSPresetTestPresetItem(
    override val index: Int,
    override val id: String
) : CSPresetItem {
    override val store = CSJsonObjectStore()
    override val title = store.property("title", "title")
    override fun save(
        properties: Iterable<CSPresetKeyData>
    ) = properties.forEach { it.saveTo(store) }

    companion object {
        val EmptyItem = CSPresetTestPresetItem(0, "")
    }
}