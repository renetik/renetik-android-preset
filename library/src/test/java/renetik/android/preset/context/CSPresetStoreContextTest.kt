package renetik.android.preset.context

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import renetik.android.core.extensions.content.createTempFile
import renetik.android.core.java.io.readString
import renetik.android.core.lang.CSEnvironment.app
import renetik.android.event.common.CSModel
import renetik.android.preset.CSPreset
import renetik.android.preset.init
import renetik.android.preset.model.CSPresetTestPresetItemList
import renetik.android.preset.model.ClearPresetItemId
import renetik.android.preset.model.NotFoundPresetItem
import renetik.android.preset.model.manageItems
import renetik.android.store.type.CSFileJsonStore
import renetik.android.testing.CSAssert.assertContains
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
class CSPresetStoreContextTest {

    @Before
    fun setUp() = Dispatchers.setMain(StandardTestDispatcher())

    @After
    fun tearDown() = Dispatchers.resetMain()

    @Test
    fun presetPropertyPresetReload() = runTest {
        val parent = CSModel()
        val presetList = CSPresetTestPresetItemList(ClearPresetItemId)
        val testFile = app.createTempFile()
        val store = CSFileJsonStore(
            testFile, isJsonPretty = false, isImmediateWrite = false
        )
        val preset = CSPreset(
            parent, store, "preset1", presetList, ::NotFoundPresetItem
        ).manageItems().init()
        val contextParent = CSModel(parent)
        val context = PresetStoreContext(
            contextParent, id = "contextId", preset, presetId = "contextKey"
        )
        val property = context.property("property", 5)
        fun checkStoreFor(content: String) {
            runBlocking { delay(2.seconds) }
            assertContains(testFile.readString()!!, content)
        }
        assertEquals(5, property.value)
//        checkStoreFor("""{"contextKey property":5}""")

        property.value = 10
        assertEquals(10, property.value)
        checkStoreFor("""{"contextKey property":10}""")

        preset.reload()
        assertEquals(5, property.value)
//        checkStoreFor("""{"contextKey property":5}""")

        context.destructClear()
        checkStoreFor("""""")
    }
}

