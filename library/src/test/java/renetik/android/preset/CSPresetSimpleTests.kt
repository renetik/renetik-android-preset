package renetik.android.preset

import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import renetik.android.event.common.CSHasRegistrationsHasDestruct
import renetik.android.event.common.CSModel
import renetik.android.preset.extensions.property
import renetik.android.preset.model.CSPresetTestPresetItemList
import renetik.android.preset.model.ClearPresetItemId
import renetik.android.preset.model.CSPresetTestItemEmpty
import renetik.android.preset.property.max
import renetik.android.store.extensions.reload
import renetik.android.store.type.CSJsonObjectStore

@RunWith(RobolectricTestRunner::class)
class CSPresetSimpleTests {
    @Test
    fun test1Max() {
        val parent: CSHasRegistrationsHasDestruct = CSModel()
        val presetList = CSPresetTestPresetItemList()

        presetList.createItem(title = ClearPresetItemId, isDefault = true)
        val store = CSJsonObjectStore()
        store.reload("""{"preset1 preset store":{"key":10}}""")
        val preset1 = CSPreset(parent, store, "preset1", presetList, CSPresetTestItemEmpty::class)
        val property = preset1.property(parent, "key", 5)
        assertEquals(10, property.value)

        val propertyMax = preset1.property(parent, "key", 5).max(7)
        assertEquals(7, propertyMax.value)
    }
}

