package renetik.android.preset

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import renetik.android.event.common.CSHasRegistrationsHasDestruct
import renetik.android.event.common.CSModel
import renetik.android.preset.CSPresetTestPresetItem.Companion.EmptyItem
import renetik.android.preset.extensions.property
import renetik.android.preset.property.max
import renetik.android.store.extensions.reload
import renetik.android.store.type.CSJsonObjectStore

@RunWith(AndroidJUnit4::class)
class CSPresetSimpleTests {
    @Test
    fun test1Max() {
        val parent: CSHasRegistrationsHasDestruct = CSModel()
        val presetList = CSPresetTestPresetItemList()

        presetList.createItem(title = ClearPresetItemId, isDefault = true)
        val store = CSJsonObjectStore()
        store.reload("""{"preset1 preset store":{"key":10}}""")
        val preset1 = CSPreset(parent, store = store, "preset1", presetList, EmptyItem)
        val property = preset1.property(parent, "key", 5)
        assertEquals(10, property.value)

        val propertyMax = preset1.property(parent, "key", 5).max(7)
        assertEquals(7, propertyMax.value)
    }
}

