package renetik.android.preset.model

import renetik.android.preset.CSPreset
import renetik.android.preset.CSPresetItem
import renetik.android.preset.property.CSPresetKeyData
import renetik.android.store.extensions.operation

fun <T : CSPreset<CSPresetItem, CSPresetTestPresetItemList>>
        T.manageItems() = apply {
    eventSave.listen { item ->
        item.store.operation {
            item.store.clear()
            for (property: CSPresetKeyData in properties)
                property.saveTo(item.store)
            for (preset: CSPreset<*,*> in presets)
                preset.store.saveTo(item.store)
        }
    }
}