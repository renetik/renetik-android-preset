package renetik.android.preset

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import renetik.android.core.kotlin.collections.at
import renetik.android.core.kotlin.collections.second
import renetik.android.core.kotlin.collections.third
import renetik.android.store.type.CSJsonObjectStore

@RunWith(AndroidJUnit4::class)
class CSPresetTest {

    private val store = CSJsonObjectStore()
    private val parent = CSPresetTestParentClass(store)

    @Test
    fun test1() {
        assertEquals(ClearPresetItemId, parent.parentPreset.listItem.value.id)
        assertEquals(ParentPropertyInitialValue, parent.property.value)
        parent.childs.forEach {
            assertEquals(ClearChildPresetItemId, it.childPreset1.listItem.value.id)
            it.childPreset1Props.forEach {
                assertEquals(ChildPropertyInitialValue, it.value)
            }
            assertEquals(ClearChildPresetItemId, it.childPreset2.listItem.value.id)
            it.childPreset2Props.forEach {
                assertEquals(ChildPropertyInitialValue, it.value)
            }
        }
        assertTrue(parent.parentPreset.store.has("childs:0 childPreset1 preset store"))
    }

    @Test
    fun test2() {
        parent.property.value = ParentPropertyNewValue1
        assertEquals(ParentPropertyNewValue1, parent.property.value)

        val item = parent.parentPreset.list
            .createItem(title = ParentPresetItemId1, isDefault = true)
        parent.parentPreset.saveAsNew(item)

        val parentPropertyNewValue2 = ParentPropertyNewValue2
        parent.property.value = parentPropertyNewValue2

        parent.parentPreset.listItem.value = parent.parentPreset.list.items.first()
        assertEquals(ParentPropertyInitialValue, parent.property.value)
    }

    @Test
    fun test3() {
        assertEquals(
            ClearChildPresetItemId, parent.childs.first()
                .childPreset1.listItem.value.id
        )
        assertEquals(
            ChildPropertyInitialValue, parent.childs.first()
                .childPreset1Props.first().value
        )
        parent.childs.first().childPreset1Props.first().value = ChildPropertyNewValue0
        assertEquals(
            ChildPropertyNewValue0, parent.childs.first()
                .childPreset1Props.first().value
        )

        val item = parent.childs.first().childPreset1.list
            .createItem(title = ChildPresetItemId2, isDefault = true)
        parent.childs.first().childPreset1.saveAsNew(item)

        assertEquals(
            ChildPropertyNewValue0, parent.childs.first()
                .childPreset1Props.first().value
        )
        assertEquals(
            ChildPresetItemId2, parent.childs.first()
                .childPreset1.listItem.value.id
        )

        parent.parentPreset.reload()
        assertEquals(
            ClearChildPresetItemId, parent.childs.first()
                .childPreset1.listItem.value.id
        )
        assertEquals(
            ChildPropertyInitialValue, parent.childs.first()
                .childPreset1Props.first().value
        )
    }

    @Test
    fun test4() {
        parent.childs.first().childPreset1Props.first().value = ChildPropertyNewValue0
        assertEquals(
            ChildPropertyNewValue0, parent.childs.first()
                .childPreset1Props.first().value
        )

        val item = parent.parentPreset.list
            .createItem(title = ParentPresetItemId1, isDefault = true)
        parent.parentPreset.saveAsNew(item)
        assertEquals(
            ChildPropertyNewValue0, parent.childs.first()
                .childPreset1Props.first().value
        )

        parent.parentPreset.listItem.value = parent.parentPreset.list.items.first()
        assertEquals(
            ChildPropertyInitialValue, parent.childs.first()
                .childPreset1Props.first().value
        )
    }

    @Test
    fun test5() {
        parent.childs.first().childPreset1Props.first().value = ChildPropertyNewValue0
        val item1 = parent.parentPreset.list.createItem(
            title = ParentPresetItemId1,
            isDefault = true
        )
        parent.parentPreset.saveAsNew(item1)
        assertEquals(
            ChildPropertyNewValue0, parent.childs.first()
                .childPreset1Props.first().value
        )

        parent.childs.first().childPreset1Props.first().value = ChildPropertyNewValue1

        val item2 = parent.parentPreset.list.createItem(
            title = ParentPresetItemId2,
            isDefault = true
        )
        parent.parentPreset.saveAsNew(item2)

        parent.parentPreset.listItem.value = parent.parentPreset.list.items.first()
        assertEquals(
            ChildPropertyInitialValue, parent.childs.first()
                .childPreset1Props.first().value
        )

        parent.parentPreset.listItem.value = parent.parentPreset.list.items.third()
        assertEquals(
            ChildPropertyNewValue1, parent.childs.first()
                .childPreset1Props.first().value
        )

        parent.parentPreset.listItem.value = parent.parentPreset.list.items.second()
        assertEquals(
            ChildPropertyNewValue0, parent.childs.first()
                .childPreset1Props.first().value
        )
    }

    @Test
    fun test6() {
        parent.childs.first().childPreset1Props.at(0)!!.value = ChildPropertyNewValue0
        parent.childs.first().childPreset1Props.at(2)!!.value = ChildPropertyNewValue2
        parent.childs.second().childPreset1Props.at(1)!!.value = ChildPropertyNewValue1
        parent.childs.second().childPreset1Props.at(3)!!.value = ChildPropertyNewValue3

        val item = parent.parentPreset.list.createItem(
            title = ParentPresetItemId1,
            isDefault = true
        )
        parent.parentPreset.saveAsNew(item)
        assertEquals(
            ChildPropertyNewValue1, parent.childs.second()
                .childPreset1Props.at(1)!!.value
        )

        parent.parentPreset.listItem.value = parent.parentPreset.list.items.first()
        assertEquals(
            ChildPropertyInitialValue, parent.childs.second()
                .childPreset1Props.at(1)!!.value
        )

        parent.parentPreset.listItem.value = parent.parentPreset.list.items.second()
        assertEquals(
            ChildPropertyNewValue2, parent.childs.first()
                .childPreset1Props.at(2)!!.value
        )
        assertEquals(
            ChildPropertyNewValue1, parent.childs.second()
                .childPreset1Props.at(1)!!.value
        )
    }

}

