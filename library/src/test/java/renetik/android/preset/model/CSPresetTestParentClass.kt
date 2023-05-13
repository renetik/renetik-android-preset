package renetik.android.preset.model

import renetik.android.event.common.CSModel
import renetik.android.preset.CSPreset
import renetik.android.preset.extensions.property
import renetik.android.store.CSStore

class CSPresetTestParentClass(store: CSStore) : CSModel() {
    private val presetList = CSPresetTestPresetItemList(ClearPresetItemId)

    val parentPreset = CSPreset(this, store, "parent", presetList, CSPresetTestItemEmpty::class)

    val children = List(4) {
        CSPresetTestChildClass(this, parentPreset, "children:$it")
    }

    val property = parentPreset.property(this, "property", ParentPropertyInitialValue)
}