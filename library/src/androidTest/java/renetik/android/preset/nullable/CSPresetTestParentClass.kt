package renetik.android.preset.nullable

import renetik.android.event.common.CSModel
import renetik.android.preset.CSPreset
import renetik.android.preset.extensions.nullIntProperty
import renetik.android.preset.nullable.CSPresetTestPresetItem.Companion.EmptyItem
import renetik.android.store.CSStore

class CSPresetTestParentClass(store: CSStore) : CSModel() {
    private val presetList = CSPresetTestPresetItemList()

    init {
        presetList.createItem(title = "Clear Parent", isDefault = true)
    }

    val parentPreset = CSPreset(
        this, store = store, "parentClass parent", presetList, EmptyItem,
    )
    val property1 = parentPreset.nullIntProperty(this, "property1", 1)
    val property2 = parentPreset.nullIntProperty(this, "property2", 2)
}