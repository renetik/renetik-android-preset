package renetik.android.preset.context

import renetik.android.core.kotlin.collections.put
import renetik.android.core.kotlin.primitives.joinToString
import renetik.android.core.lang.ArgFunc
import renetik.android.core.lang.CSHasId
import renetik.android.event.common.CSHasDestruct
import renetik.android.event.common.CSModel
import renetik.android.event.common.onDestructed
import renetik.android.preset.CSPreset
import renetik.android.preset.Preset
import renetik.android.preset.extensions.nullFloatProperty
import renetik.android.preset.extensions.nullIntProperty
import renetik.android.preset.extensions.nullListItemProperty
import renetik.android.preset.extensions.nullStringProperty
import renetik.android.preset.extensions.property
import renetik.android.preset.property.CSPresetProperty
import renetik.android.store.context.StoreContext
import renetik.android.store.extensions.operation

class PresetStoreContext(
    parent: CSHasDestruct,
    override val id: String,
    val preset: Preset,
    val presetId: String? = null,
    override val key: String? = presetId
) : CSModel(parent), StoreContext {

    companion object {
        fun PresetStoreContext(
            parent: CSHasDestruct, hasId: CSHasId, preset: Preset, key: String? = null,
        ) = PresetStoreContext(
            parent, id = key?.let { "${hasId.id} $it" } ?: hasId.id,
            preset, presetId = key, key
        )
    }

    private val childContexts = mutableListOf<StoreContext>()

    private fun <T : StoreContext> T.init() = also {
        childContexts += this
        it.onDestructed { if (!isDestructed) childContexts -= this }
    }

    override fun context(parent: CSHasDestruct, key: String?): PresetStoreContext =
        PresetStoreContext(parent,
            id = (id to key).joinToString(" "), preset,
            presetId = (presetId to key).joinToString(" "), key
        ).init()


    fun context(parent: CSHasDestruct, id: String, presetId: String): PresetStoreContext =
        PresetStoreContext(parent,
            id = "${this.id} $id", preset,
            presetId = (this.presetId to presetId).joinToString(" "), key
        ).init()

    override fun appContext(parent: CSHasDestruct, key: String?) =
        AppStoreContext(parent, this, key).init()

    override fun memoryContext(parent: CSHasDestruct, key: String?) =
        RuntimeStoreContext(parent, this, key).init()

    override fun onChange(function: (Unit) -> Unit) = preset.onChange(function)

    private val keys = mutableListOf<String>()

    private val properties = mutableListOf<CSPresetProperty<*>>()
    private fun <T : CSPresetProperty<*>> T.init() = also {
        properties += this
        it.onDestructed { if (!isDestructed) properties -= this }
    }

    private val presets = mutableListOf<CSPreset<*, *>>()

    fun add(preset: CSPreset<*, *>) {
        presets += preset
        preset.onDestructed { if (!isDestructed) presets -= preset }
    }

    override fun clear(): Unit = preset.store.operation {
        properties.forEach(CSPresetProperty<*>::clear)
        childContexts.onEach(StoreContext::clear)
        presets.onEach(CSPreset<*, *>::clear)
    }

    private val String.newKey
        get() = keys.put(presetId?.let { "$it $this" } ?: this)

    override fun property(
        key: String, default: String, onChange: ArgFunc<String>?,
    ) = preset.property(
        this, key.newKey, default, onChange
    ).init()

    override fun property(
        key: String, default: Boolean, onChange: ArgFunc<Boolean>?,
    ) = preset.property(
        this, key.newKey, default, onChange
    ).init()

    override fun property(
        key: String, default: Float, onChange: ArgFunc<Float>?,
    ) = preset.property(
        this, key.newKey, default, onChange
    ).init()

    override fun property(
        key: String, default: Int, onChange: ArgFunc<Int>?
    ) = preset.property(
        this, key.newKey, default, onChange
    ).init()

    override fun <T> property(
        key: String, values: () -> List<T>, default: () -> T, onChange: ArgFunc<T>?
    ) = preset.property(
        this, key.newKey, values, default, onChange
    ).init()

    override fun nullIntProperty(
        key: String, default: Int?, onChange: ((value: Int?) -> Unit)?
    ) = preset.nullIntProperty(
        this, key.newKey, default, onChange
    ).init()

    override fun nullFloatProperty(
        key: String, default: Float?, onChange: ((value: Float?) -> Unit)?
    ) = preset.nullFloatProperty(
        this, key.newKey, default, onChange
    ).init()

    override fun nullStringProperty(
        key: String, default: String?, onChange: ((value: String?) -> Unit)?
    ) = preset.nullStringProperty(
        this, key.newKey, default, onChange
    ).init()

    override fun <T> nullListItemProperty(
        key: String, values: List<T>, default: T?, onChange: ((value: T?) -> Unit)?
    ) = preset.nullListItemProperty(
        this, key.newKey, values, default, onChange
    ).init()

    override fun property(
        key: String, default: List<Int>, onChange: ArgFunc<List<Int>>?
    ) = preset.property(
        this, key.newKey, default, onChange
    ).init()
}