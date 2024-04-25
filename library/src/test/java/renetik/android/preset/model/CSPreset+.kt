package renetik.android.preset.model

import renetik.android.preset.CSPreset
import renetik.android.preset.CSPresetItem
import renetik.android.preset.property.CSPresetKeyData

fun <T : CSPreset<CSPresetItem, CSPresetTestPresetItemList>>
        T.manageItems() = apply {
    eventSave.listen { item ->
        item.store.operation {
            item.store.clear()
            for (property: CSPresetKeyData in data) property.saveTo(item.store)
        }
    }
}