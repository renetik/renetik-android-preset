package renetik.android.preset

import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import renetik.android.core.lang.variable.assign
import renetik.android.event.common.CSModel
import renetik.android.json.CSJson
import renetik.android.json.toJson
import renetik.android.preset.CSPreset.Companion.CSPreset
import renetik.android.preset.extensions.property
import renetik.android.preset.model.NotFoundPresetItem
import renetik.android.preset.model.TestCSPresetItemList
import renetik.android.preset.model.manageItems
import renetik.android.store.extensions.reload
import renetik.android.store.type.CSJsonObjectStore

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
class LoadChildChildTest {
    @Before fun setUp() {
        CSJson.isJsonPretty = true
    }

    val preset = CSPreset(
        CSModel(), CSJsonObjectStore(), "preset",
        TestCSPresetItemList(firstItemId = "clear preset item"),
        ::NotFoundPresetItem
    ).manageItems().init()

    val childPresetList = TestCSPresetItemList(firstItemId = "clear childPreset item")
    val secondChildPresetListItem = childPresetList.createItem(
        "second childPreset item", isDefault = true).also {
        it.store.reload("""{
    "childChildPreset preset current": "second childChildPreset item"
  }""")
    }
    val thirdChildPresetListItem = childPresetList.createItem(
        "third childPreset item", isDefault = true).also {
        it.store.reload("""{
    "childChildPreset preset current": "second childChildPreset item",
    "childChildPreset preset store": {
      "childChildPresetProperty": 10
    }
  }""")
    }
    val childPreset = CSPreset(
        CSModel(), preset, "childPreset", childPresetList, ::NotFoundPresetItem,
    ).manageItems().init()

    val childChildPresetList = TestCSPresetItemList(
        firstItemId = "clear childChildPreset item")

    val secondChildChildPresetListItem = childChildPresetList.createItem(
        "second childChildPreset item", isDefault = true).also {
        it.store.reload("""{
      "childChildPresetProperty": 10
    }""")
    }

    val childChildPreset = CSPreset(
        CSModel(), childPreset, "childChildPreset", childChildPresetList, ::NotFoundPresetItem,
    ).manageItems().init()

    val childChildPresetProperty = childChildPreset.property(CSModel(),
        "childChildPresetProperty", 5)

    val initialStoreJson = """{
  "childPreset preset current": "clear childPreset item",
  "childPreset preset store": {
    "childChildPreset preset current": "clear childChildPreset item",
    "childChildPreset preset store": {}
  }
}"""
    val clearChildSecondChildChildStoreChason = """{
  "childPreset preset current": "clear childPreset item",
  "childPreset preset store": {
    "childChildPreset preset current": "second childChildPreset item",
    "childChildPreset preset store": {
      "childChildPresetProperty": 10
    }
  }
}"""

    val secondsChildStoreChason = """{
  "childPreset preset current": "second childPreset item",
  "childPreset preset store": {
    "childChildPreset preset current": "second childChildPreset item",
    "childChildPreset preset store": {
      "childChildPresetProperty": 10
    }
  }
}"""

    @Test
    fun loadSecondChildChildPresetListItem() {
        assertEquals(5, childChildPresetProperty.value)
        assertEquals(preset.store.data.toJson(), initialStoreJson)
        childChildPreset.listItem assign secondChildChildPresetListItem
        assertEquals(preset.store.data.toJson(), clearChildSecondChildChildStoreChason)
        assertEquals(10, childChildPresetProperty.value)
        childPreset.reload()
        assertEquals(preset.store.data.toJson(), initialStoreJson)
        assertEquals(5, childChildPresetProperty.value)
        childChildPreset.listItem assign secondChildChildPresetListItem
        assertEquals(preset.store.data.toJson(), clearChildSecondChildChildStoreChason)
        assertEquals(10, childChildPresetProperty.value)
        preset.reload()
        assertEquals(preset.store.data.toJson(), initialStoreJson)
        assertEquals(5, childChildPresetProperty.value)
    }

    @Test
    fun loadSecondChildPresetListItem() {
        assertEquals(5, childChildPresetProperty.value)
        assertEquals(preset.store.data.toJson(), initialStoreJson)
        childPreset.listItem assign secondChildPresetListItem
        assertEquals(preset.store.data.toJson(), secondsChildStoreChason)
    }
}