package renetik.android.preset.property.nullable

import renetik.android.store.type.CSJsonObjectStore

class CSPresetTestPresetItem(override val id: String) : renetik.android.preset.CSPresetItem {
	override val store = CSJsonObjectStore()
	override fun save(properties: Iterable<renetik.android.preset.property.CSPresetKeyData>) =
		properties.forEach { it.saveTo(store) }
}