package renetik.android.preset

import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import renetik.android.event.common.CSModel
import renetik.android.json.toJson
import renetik.android.preset.extensions.property
import renetik.android.preset.model.CSPresetTestItemEmpty
import renetik.android.preset.model.CSPresetTestPresetItemList
import renetik.android.preset.model.ClearPresetItemId
import renetik.android.preset.property.max
import renetik.android.store.extensions.reload
import renetik.android.store.type.CSJsonObjectStore
import renetik.android.testing.CSAssert.assertContains

@RunWith(RobolectricTestRunner::class)
class CSPresetSimpleTests {
    @Test
    fun presetPropertyPresetReload() {
        val parent = CSModel()
        val presetList = CSPresetTestPresetItemList(ClearPresetItemId)

        val store = CSJsonObjectStore()
        val preset = CSPreset(parent, store, "preset1", presetList, CSPresetTestItemEmpty::class)
        val property = preset.property(parent, "preset1 property", 5)

        assertEquals(5, property.value)

        property.value = 10
        assertEquals(10, property.value)

        preset.reload()
        assertEquals(5, property.value)
    }

    @Test
    fun childPresetPropertyPresetReload() {
        val parent = CSModel()
        val presetList = CSPresetTestPresetItemList(ClearPresetItemId)

        val preset = CSPreset(
            parent, CSJsonObjectStore(), "preset", presetList, CSPresetTestItemEmpty::class
        )
        val childPreset = CSPreset(
            parent, preset, "childPreset", presetList, CSPresetTestItemEmpty::class
        )
        val childPresetProperty = childPreset.property(parent, "preset1 property", 5)
        assertEquals(5, childPresetProperty.value)

        childPresetProperty.value = 10
        assertEquals(10, childPresetProperty.value)

        preset.reload()
        assertEquals(5, childPresetProperty.value)
    }

    @Test
    fun childPresetPropertySaveAsNewItemPresetReload() {
        val parent = CSModel()
        val preset = CSPreset(
            parent, CSJsonObjectStore(), "preset",
            CSPresetTestPresetItemList(clearItemId = "clear preset item"),
            CSPresetTestItemEmpty::class
        )
        val childPreset = CSPreset(
            parent, preset, "childPreset",
            CSPresetTestPresetItemList(clearItemId = "clear childPreset item"),
            CSPresetTestItemEmpty::class
        )
        val childPresetProperty = childPreset.property(parent, "childPresetProperty", 5)
        assertEquals(5, childPresetProperty.value)
        assertContains(
            preset.store.data.toJson(),
            """"childPreset preset current":"clear childPreset item"""",
            """"childPreset preset store":{"childPresetProperty":5}}""",
        )

        childPresetProperty.value = 10
        assertEquals(10, childPresetProperty.value)
        val item = childPreset.list.createItem("childPresetProperty item", isDefault = true)
        childPreset.saveAsNew(item)

        assertContains(
            preset.store.data.toJson(),
            """"childPreset preset current":"childPresetProperty item"""",
            """"childPreset preset store":{"childPresetProperty":10}}"""
        )
        preset.reload()
        assertContains(
            preset.store.data.toJson(),
            """"childPreset preset current":"clear childPreset item"""",
            """"childPreset preset store":{"childPresetProperty":5}}""",
        )
        assertEquals(5, childPresetProperty.value)
    }

    @Test
    fun presetPropertyMax() {
        val parent = CSModel()
        val presetList = CSPresetTestPresetItemList(ClearPresetItemId)

        val store = CSJsonObjectStore()
        store.reload("""{"preset preset store":{"key":10}}""")
        val preset = CSPreset(parent, store, "preset", presetList, CSPresetTestItemEmpty::class)

        val property = preset.property(parent, "key", 5)
        assertEquals(10, property.value)

        val propertyMax = preset.property(parent, "key", 5).max(7)
        assertEquals(7, propertyMax.value)
    }

}

