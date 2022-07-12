package renetik.android.preset

import renetik.android.store.type.CSJsonObjectStore

class CSPresetTestPresetItem(override val id: String) : CSPresetItem {
    override val store = CSJsonObjectStore()
    override fun save(properties: Iterable<renetik.android.preset.property.CSPresetKeyData>) =
        properties.forEach { it.saveTo(store) }

    override fun toString() = "${super.toString()}, id:$id"
}