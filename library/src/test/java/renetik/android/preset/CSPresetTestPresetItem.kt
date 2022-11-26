package renetik.android.preset

import renetik.android.preset.property.CSPresetKeyData
import renetik.android.store.type.CSJsonObjectStore

class CSPresetTestPresetItem(
    override val index: Int,
    override val id: String) : CSPresetItem {
    override val store = CSJsonObjectStore()
    override fun save(properties: Iterable<CSPresetKeyData>) =
        properties.forEach { it.saveTo(store) }

    override fun toString() = "${super.toString()}, id:$id"
}