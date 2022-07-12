package renetik.android.preset

import renetik.android.event.common.CSModel
import renetik.android.preset.extensions.property
import renetik.android.store.CSStore

class CSPresetTestParentClass(store: CSStore) : CSModel() {
   private val presetList = CSPresetTestPresetItemList()

   init {
       presetList.add(CSPresetTestPresetItem(ClearPresetItemId))
   }

   val parentPreset = CSPreset(this, store, "parent", presetList)

   val childs = List(4) {
       CSPresetTestChildClass(this, parentPreset, "childs:$it")
   }

   val property = parentPreset.property(this, "property", ParentPropertyInitialValue)
}