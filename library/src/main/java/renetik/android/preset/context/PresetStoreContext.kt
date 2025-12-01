package renetik.android.preset.context

import renetik.android.core.kotlin.primitives.joinToString
import renetik.android.core.lang.ArgFun
import renetik.android.core.lang.CSHasId
import renetik.android.event.common.CSHasDestruct
import renetik.android.event.common.CSModel
import renetik.android.event.registration.invoke
import renetik.android.json.obj.CSJsonObjectInterface
import renetik.android.preset.CSPreset
import renetik.android.preset.Preset
import renetik.android.preset.extensions.nullFloatProperty
import renetik.android.preset.extensions.nullIntProperty
import renetik.android.preset.extensions.nullListItemProperty
import renetik.android.preset.extensions.nullStringProperty
import renetik.android.preset.extensions.property
import renetik.android.preset.property.CSPresetProperty
import renetik.android.store.context.CSStoreContext
import renetik.android.store.extensions.operation

class PresetStoreContext(
    parent: CSHasDestruct,
    override val id: String,
    val preset: Preset,
    val presetId: String? = null,
    override val key: String? = presetId
) : CSModel(parent), CSStoreContext {

    companion object {
        fun PresetStoreContext(
            parent: CSHasDestruct, hasId: CSHasId, preset: Preset, key: String? = null,
        ) = PresetStoreContext(
            parent, id = key?.let { "${hasId.id} $it" } ?: hasId.id,
            preset, presetId = key, key
        )
    }

    override val data: CSJsonObjectInterface = preset.store

    private val childContexts = mutableListOf<CSStoreContext>()

    private fun <T : CSStoreContext> T.init(parent: PresetStoreContext) = apply {
        parent.childContexts += this
        eventDestruct { if (!parent.isDestructed) parent.childContexts -= this }
    }

    override fun context(parent: CSHasDestruct, key: String?): PresetStoreContext =
        PresetStoreContext(parent,
            id = (id to key).joinToString(" "), preset,
            presetId = (presetId to key).joinToString(" "), key
        ).init(this)


    fun context(parent: CSHasDestruct, id: String, presetId: String): PresetStoreContext =
        PresetStoreContext(parent,
            id = "${this.id} $id", preset,
            presetId = (this.presetId to presetId).joinToString(" "), key
        ).init(this)

    override fun appContext(parent: CSHasDestruct, key: String?) =
        AppStoreContext(parent, this, key).init(this)

    override fun memoryContext(parent: CSHasDestruct, key: String?) =
        RuntimeStoreContext(parent, this, key).init(this)

    override fun onChange(function: (Unit) -> Unit) = preset.onChange(function)

// TODO?: Could be finished and used if necessary
//    override fun equals(other: Any?): Boolean = (other as? PresetStoreContext)?.let {
//    properties.all { (key, property) -> it.properties[key]?.value == property.value }
//    } ?: super.equals(other)

    private val properties = mutableMapOf<String, CSPresetProperty<*>>()
    private fun <T : CSPresetProperty<*>> T.init(key: String) = apply {
        properties[key] = this
    }

    private val presets = mutableListOf<CSPreset<*, *>>()

    fun add(preset: CSPreset<*, *>) {
        presets += preset
        preset.eventDestruct { if (!isDestructed) presets -= preset }
    }

    override fun clear(): Unit = preset.store.operation {
        properties.values.forEach { it.clear() }
        childContexts.toList().onEach { it.clear() }
        presets.toList().onEach { it.clear() }
    }

    private val String.newKey: String
        get() = presetId?.let { "$it $this" } ?: this

    override fun property(
        key: String, default: String, onChange: ArgFun<String>?,
    ) = preset.property(
        this, key.newKey, default, onChange
    ).init(key)

    override fun property(
        key: String, default: Boolean, onChange: ArgFun<Boolean>?,
    ) = preset.property(
        this, key.newKey, default, onChange
    ).init(key)

    override fun property(
        key: String, default: Float, onChange: ArgFun<Float>?,
    ) = preset.property(
        this, key.newKey, default, onChange
    ).init(key)

    override fun property(
        key: String, default: Int, onChange: ArgFun<Int>?
    ) = preset.property(
        this, key.newKey, default, onChange
    ).init(key)

    override fun property(
        key: String, default: () -> Int, onChange: ArgFun<Int>?
    ) = preset.property(
        this, key.newKey, default, onChange
    ).init(key)

    override fun <T> property(
        key: String, values: () -> Collection<T>,
        default: () -> T, onChange: ArgFun<T>?
    ) = preset.property(
        this, key.newKey, values, default, onChange
    ).init(key)

    override fun nullIntProperty(
        key: String, default: Int?, onChange: ((value: Int?) -> Unit)?
    ) = preset.nullIntProperty(
        this, key.newKey, default, onChange
    ).init(key)

    override fun nullFloatProperty(
        key: String, default: Float?, onChange: ((value: Float?) -> Unit)?
    ) = preset.nullFloatProperty(
        this, key.newKey, default, onChange
    ).init(key)

    override fun nullStringProperty(
        key: String, default: String?, onChange: ((value: String?) -> Unit)?
    ) = preset.nullStringProperty(
        this, key.newKey, default, onChange
    ).init(key)

    override fun <T> nullListItemProperty(
        key: String, values: List<T>, default: T?, onChange: ((value: T?) -> Unit)?
    ) = preset.nullListItemProperty(
        this, key.newKey, values, default, onChange
    ).init(key)

    override fun property(
        key: String, default: List<Int>, onChange: ArgFun<List<Int>>?
    ) = preset.property(
        this, key.newKey, default, onChange
    ).init(key)

    override fun <T : CSHasId> property(
        key: String, values: List<T>,
        default: List<T>, onChange: ArgFun<List<T>>?
    ) = preset.property(
        this, key.newKey, default, values, onChange
    ).init(key)
}