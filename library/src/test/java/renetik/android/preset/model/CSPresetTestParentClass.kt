package renetik.android.preset.model

import renetik.android.core.kotlin.primitives.ends
import renetik.android.core.lang.catchAllWarn
import renetik.android.event.common.CSModel
import renetik.android.event.registration.CSHasRegistrations
import renetik.android.event.registration.plus
import renetik.android.preset.CSPreset
import renetik.android.preset.CSPresetStore
import renetik.android.preset.extensions.property
import renetik.android.preset.init
import renetik.android.preset.property.CSPresetKeyData
import renetik.android.store.CSStore
import renetik.android.store.extensions.operation
import renetik.android.store.property.saveTo

class CSPresetTestParentClass(store: CSStore) : CSModel() {
    private val presetList = CSPresetTestPresetItemList(ClearPresetItemId)

    val parentPreset = CSPreset(
        this, store, "parent", presetList, ::NotFoundInstPresetItem
    ).manageItems().init()

    val children = List(4) {
        CSPresetTestChildClass(this, parentPreset, "children:$it")
    }

    val property = parentPreset.property(this, "property", ParentPropertyInitialValue)
}