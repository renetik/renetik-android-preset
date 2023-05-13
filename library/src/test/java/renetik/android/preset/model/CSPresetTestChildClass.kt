package renetik.android.preset.model

import renetik.android.event.common.CSModel
import renetik.android.preset.CSPreset
import renetik.android.preset.extensions.property

class CSPresetTestChildClass(
    parent: CSPresetTestParentClass,
    preset: CSPreset<*, *>,
    key: String
) : CSModel(parent) {

    private val presetList = CSPresetTestPresetItemList()

    init {
        presetList.createItem(title = ClearChildPresetItemId, isDefault = true)
    }

    val childPreset1 = CSPreset(
        this, preset, "$key childPreset1", presetList, CSPresetTestItemEmpty::class
    )
    val childPreset1Props = List(4) {
        childPreset1.property(
            this, "$key childPreset1Props:$it property", ChildPropertyInitialValue
        )
    }

    val childPreset2 = CSPreset(
        this, preset, "$key childPreset2", presetList, CSPresetTestItemEmpty::class
    )
    val childPreset2Props = List(4) {
        childPreset2.property(
            this, "$key childPreset2Props:$it property",
            ChildPropertyInitialValue
        )
    }
}