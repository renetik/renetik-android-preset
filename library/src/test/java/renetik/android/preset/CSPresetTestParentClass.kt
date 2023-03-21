package renetik.android.preset

import renetik.android.event.common.CSModel
import renetik.android.preset.CSPresetTestPresetItem.Companion.EmptyItem
import renetik.android.preset.extensions.property
import renetik.android.store.CSStore

class CSPresetTestParentClass(store: CSStore) : CSModel() {
    private val presetList = CSPresetTestPresetItemList()

    init {
        presetList.createItem(title = ClearPresetItemId, isDefault = true)
    }

    val parentPreset = CSPreset(this, store = store, "parent", presetList, EmptyItem)

    val childs = List(4) {
        CSPresetTestChildClass(this, parentPreset, "childs:$it")
    }

    val property = parentPreset.property(this, "property", ParentPropertyInitialValue)
}