package renetik.android.preset

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import renetik.android.core.kotlin.collections.at
import renetik.android.core.kotlin.collections.second
import renetik.android.core.kotlin.collections.third
import renetik.android.preset.model.CSPresetTestParentClass
import renetik.android.preset.model.ChildPresetItemId2
import renetik.android.preset.model.ChildPropertyInitialValue
import renetik.android.preset.model.ChildPropertyNewValue0
import renetik.android.preset.model.ChildPropertyNewValue1
import renetik.android.preset.model.ChildPropertyNewValue2
import renetik.android.preset.model.ChildPropertyNewValue3
import renetik.android.preset.model.ClearChildPresetItemId
import renetik.android.preset.model.ClearPresetItemId
import renetik.android.preset.model.ParentPresetItemId1
import renetik.android.preset.model.ParentPresetItemId2
import renetik.android.preset.model.ParentPropertyInitialValue
import renetik.android.preset.model.ParentPropertyNewValue1
import renetik.android.preset.model.ParentPropertyNewValue2
import renetik.android.store.type.CSJsonObjectStore

@Deprecated("Replace all cases by simple tests")
@RunWith(RobolectricTestRunner::class)
class CSPresetTest {

    private val store = CSJsonObjectStore()
    private val parent = CSPresetTestParentClass(store)

    @Test
    fun test1() {
        assertEquals(ClearPresetItemId, parent.parentPreset.listItem.value.id)
        assertEquals(ParentPropertyInitialValue, parent.property.value)
        parent.children.forEach {
            assertEquals(ClearChildPresetItemId, it.childPreset1.listItem.value.id)
            it.childPreset1Props.forEach {
                assertEquals(ChildPropertyInitialValue, it.value)
            }
            assertEquals(ClearChildPresetItemId, it.childPreset2.listItem.value.id)
            it.childPreset2Props.forEach {
                assertEquals(ChildPropertyInitialValue, it.value)
            }
        }
        assertTrue(parent.parentPreset.store.has("children:0 childPreset1 preset store"))
    }

    @Test
    fun test2() {
        parent.property.value = ParentPropertyNewValue1
        assertEquals(ParentPropertyNewValue1, parent.property.value)

        val item = parent.parentPreset.list
            .createItem(ParentPresetItemId1, isDefault = true)
        parent.parentPreset.saveAsNew(item)

        val parentPropertyNewValue2 = ParentPropertyNewValue2
        parent.property.value = parentPropertyNewValue2

        parent.parentPreset.listItem.value = parent.parentPreset.list.items.first()
        assertEquals(ParentPropertyInitialValue, parent.property.value)
    }

    @Test
    fun test3() {
        assertEquals(
            ClearChildPresetItemId, parent.children.first()
                .childPreset1.listItem.value.id
        )
        assertEquals(
            ChildPropertyInitialValue, parent.children.first()
                .childPreset1Props.first().value
        )
        parent.children.first().childPreset1Props.first().value = ChildPropertyNewValue0
        assertEquals(
            ChildPropertyNewValue0, parent.children.first()
                .childPreset1Props.first().value
        )

        val item = parent.children.first().childPreset1.list
            .createItem(ChildPresetItemId2, isDefault = true)
        parent.children.first().childPreset1.saveAsNew(item)

        assertEquals(
            ChildPropertyNewValue0, parent.children.first()
                .childPreset1Props.first().value
        )
        assertEquals(
            ChildPresetItemId2, parent.children.first()
                .childPreset1.listItem.value.id
        )

        parent.parentPreset.reload()
        assertEquals(
            ClearChildPresetItemId, parent.children.first()
                .childPreset1.listItem.value.id
        )
        assertEquals(
            ChildPropertyInitialValue, parent.children.first()
                .childPreset1Props.first().value
        )
    }

    @Test
    fun test4() {
        parent.children.first().childPreset1Props.first().value = ChildPropertyNewValue0
        assertEquals(
            ChildPropertyNewValue0, parent.children.first()
                .childPreset1Props.first().value
        )

        val item = parent.parentPreset.list.createItem(ParentPresetItemId1, isDefault = true)
        parent.parentPreset.saveAsNew(item)
        assertEquals(
            ChildPropertyNewValue0, parent.children.first()
                .childPreset1Props.first().value
        )

        parent.parentPreset.listItem.value = parent.parentPreset.list.items.first()
        assertEquals(
            ChildPropertyInitialValue, parent.children.first()
                .childPreset1Props.first().value
        )
    }

    @Test
    fun test5() {
        parent.children.first().childPreset1Props.first().value = ChildPropertyNewValue0
        val item1 = parent.parentPreset.list.createItem(ParentPresetItemId1, isDefault = true)
        parent.parentPreset.saveAsNew(item1)
        assertEquals(
            ChildPropertyNewValue0, parent.children.first()
                .childPreset1Props.first().value
        )

        parent.children.first().childPreset1Props.first().value = ChildPropertyNewValue1

        val item2 = parent.parentPreset.list.createItem(ParentPresetItemId2, isDefault = true)
        parent.parentPreset.saveAsNew(item2)

        parent.parentPreset.listItem.value = parent.parentPreset.list.items.first()
        assertEquals(
            ChildPropertyInitialValue, parent.children.first()
                .childPreset1Props.first().value
        )

        parent.parentPreset.listItem.value = parent.parentPreset.list.items.third()
        assertEquals(
            ChildPropertyNewValue1, parent.children.first()
                .childPreset1Props.first().value
        )

        parent.parentPreset.listItem.value = parent.parentPreset.list.items.second()
        assertEquals(
            ChildPropertyNewValue0, parent.children.first()
                .childPreset1Props.first().value
        )
    }

    @Test
    fun test6() {
        parent.children.first().childPreset1Props.at(0)!!.value = ChildPropertyNewValue0
        parent.children.first().childPreset1Props.at(2)!!.value = ChildPropertyNewValue2
        parent.children.second().childPreset1Props.at(1)!!.value = ChildPropertyNewValue1
        parent.children.second().childPreset1Props.at(3)!!.value = ChildPropertyNewValue3

        val item = parent.parentPreset.list.createItem(ParentPresetItemId1, isDefault = true)
        parent.parentPreset.saveAsNew(item)
        assertEquals(
            ChildPropertyNewValue1, parent.children.second()
                .childPreset1Props.at(1)!!.value
        )

        parent.parentPreset.listItem.value = parent.parentPreset.list.items.first()
        assertEquals(
            ChildPropertyInitialValue, parent.children.second()
                .childPreset1Props.at(1)!!.value
        )

        parent.parentPreset.listItem.value = parent.parentPreset.list.items.second()
        assertEquals(
            ChildPropertyNewValue2, parent.children.first()
                .childPreset1Props.at(2)!!.value
        )
        assertEquals(
            ChildPropertyNewValue1, parent.children.second()
                .childPreset1Props.at(1)!!.value
        )
    }

}

