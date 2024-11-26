package renetik.android.preset.context

import renetik.android.core.lang.ArgFunc
import renetik.android.core.lang.CSHasId
import renetik.android.event.common.CSHasDestruct
import renetik.android.event.common.CSModel
import renetik.android.event.common.onDestructed
import renetik.android.event.common.parent
import renetik.android.event.registration.CSRegistration
import renetik.android.preset.CSPreset
import renetik.android.store.CSStore
import renetik.android.store.context.StoreContext
import renetik.android.store.extensions.nullFloatProperty
import renetik.android.store.extensions.nullIntProperty
import renetik.android.store.extensions.nullListItemProperty
import renetik.android.store.extensions.nullStringProperty
import renetik.android.store.extensions.operation
import renetik.android.store.extensions.property
import renetik.android.store.property.CSStoreProperty
import renetik.android.store.property.listenStore
import renetik.android.store.property.value.CSIntListValueStoreProperty

class CustomStoreContext(
    parent: CSHasDestruct? = null,
    val store: CSStore,
    private val hasId: CSHasId? = null,
    override val key: String? = null,
) : CSModel(parent), StoreContext {
    override val id = hasId?.let { "${it.id} $key" } ?: key ?: ""
    private val childContexts = mutableListOf<StoreContext>()

    private fun <T : StoreContext> T.init() = apply {
        childContexts += this; onDestructed { childContexts -= this }
    }

    override fun context(parent: CSHasDestruct, key: String?) =
        (key?.let { CustomStoreContext(parent, store, this, key) }
            ?: CustomStoreContext(parent, store, hasId, this.key)).init()

    override fun appContext(parent: CSHasDestruct, key: String?) =
        AppStoreContext(parent, this, key).init()

    override fun memoryContext(parent: CSHasDestruct, key: String?) =
        RuntimeStoreContext(parent, this, key).init()

    override fun onChange(function: (Unit) -> Unit): CSRegistration =
        store.eventLoaded.listen { function(Unit) }


    private val properties = mutableListOf<CSStoreProperty<*>>()
    private fun <T : CSStoreProperty<*>> T.init() = apply {
        properties += this; onDestructed { properties -= this }
    }

    private val presets = mutableListOf<CSPreset<*, *>>()

    fun add(preset: CSPreset<*, *>) {
        presets += preset; preset.onDestructed { presets -= preset }
    }

    override fun clear() = store.operation {
        properties.forEach(CSStoreProperty<*>::clear)
        childContexts.onEach(StoreContext::clear)
        presets.onEach(CSPreset<*, *>::clear)
    }

    private val String.newKey get() = "$id $this"

    override fun property(
        key: String, default: String, onChange: ArgFunc<String>?,
    ) = store.property(this, key.newKey, default, onChange).init()

    override fun property(
        key: String, default: Boolean, onChange: ArgFunc<Boolean>?,
    ) = store.property(this, key.newKey, default, onChange).init()

    override fun property(
        key: String, default: Float, onChange: ArgFunc<Float>?,
    ) = store.property(this, key.newKey, default, onChange).init()

    override fun property(
        key: String, default: Int, onChange: ArgFunc<Int>?
    ) = store.property(this, key.newKey, default, onChange).init()

    override fun <T> property(
        key: String, values: () -> List<T>, default: () -> T, onChange: ArgFunc<T>?
    ) = store.property(this, key.newKey, values, default, onChange).init()

    override fun nullIntProperty(
        key: String, default: Int?, onChange: ((value: Int?) -> Unit)?
    ) = store.nullIntProperty(this, key.newKey, default, onChange).init()

    override fun nullFloatProperty(
        key: String, default: Float?, onChange: ((value: Float?) -> Unit)?
    ) = store.nullFloatProperty(this, key.newKey, default, onChange).init()

    override fun nullStringProperty(
        key: String, default: String?, onChange: ((value: String?) -> Unit)?
    ) = store.nullStringProperty(this, key.newKey, default, onChange).init()

    override fun <T> nullListItemProperty(
        key: String, values: List<T>, default: T?, onChange: ((value: T?) -> Unit)?
    ) = store.nullListItemProperty(key.newKey, values, default, onChange)
        .parent(this).listenStore().init()

    override fun property(
        key: String, default: List<Int>, onChange: ArgFunc<List<Int>>?
    ) = CSIntListValueStoreProperty(store, key.newKey, default, onChange)
        .parent(this).listenStore().init()
}