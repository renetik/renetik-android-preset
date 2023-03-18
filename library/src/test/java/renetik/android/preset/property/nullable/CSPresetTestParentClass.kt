package renetik.android.preset.property.nullable

import renetik.android.event.common.CSModel
import renetik.android.preset.CSPreset
import renetik.android.preset.extensions.nullIntProperty
import renetik.android.store.CSStore

class CSPresetTestParentClass(store: CSStore) : CSModel() {
    private val presetList = CSPresetTestPresetItemList()

    init {
        presetList.createItem(title = "Clear Parent", isDefault = true)
    }

    val parentPreset = CSPreset(this, store, "parentClass parent", presetList)
    val property1 = parentPreset.nullIntProperty(this, "property1", 1)
    val property2 = parentPreset.nullIntProperty(this, "property2", 2)
}