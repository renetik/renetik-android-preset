package renetik.android.preset

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import renetik.android.core.lang.variable.assign
import renetik.android.event.common.CSModel
import renetik.android.json.toJson
import renetik.android.preset.CSPreset.Companion.CSPreset
import renetik.android.preset.extensions.property
import renetik.android.preset.model.NotFoundPresetItem
import renetik.android.preset.model.TestCSPresetItemList
import renetik.android.preset.model.manageItems
import renetik.android.store.type.CSJsonObjectStore

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
class LoadChildChildTest {
    @Before fun setUp() = Dispatchers.setMain(StandardTestDispatcher())
    @After fun tearDown() = Dispatchers.resetMain()

    val preset = CSPreset(
        CSModel(), CSJsonObjectStore(), "preset",
        TestCSPresetItemList(firstItemId = "clear preset item"),
        ::NotFoundPresetItem
    ).manageItems().init()

    val childPreset = CSPreset(
        CSModel(), preset, "childPreset",
        TestCSPresetItemList(firstItemId = "clear childPreset item"),
        ::NotFoundPresetItem,
    ).manageItems().init()

    val childChildPreset = CSPreset(
        CSModel(), childPreset, "childChildPreset",
        TestCSPresetItemList(firstItemId = "clear childChildPreset item"),
        ::NotFoundPresetItem,
    ).manageItems().init()

    val childChildPresetProperty = childChildPreset.property(CSModel(),
        "childChildPresetProperty", 5)

    @Test
    fun loadChildChildTest() {
        assertEquals(5, childChildPresetProperty.value)
        assertEquals(preset.store.data.toJson(formatted = true), """{
  "childPreset preset current": "clear childPreset item",
  "childPreset preset store": {
    "childChildPreset preset current": "clear childChildPreset item",
    "childChildPreset preset store": {}
  }
}""")
        childChildPresetProperty assign 10
        assertEquals(preset.store.data.toJson(formatted = true), """{
  "childPreset preset current": "clear childPreset item",
  "childPreset preset store": {
    "childChildPreset preset current": "clear childChildPreset item",
    "childChildPreset preset store": {
      "childChildPresetProperty": 10
    }
  }
}""")
    }
}