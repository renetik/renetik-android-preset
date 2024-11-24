package renetik.android.preset.context

import renetik.android.core.kotlin.collections.put
import renetik.android.core.lang.ArgFunc
import renetik.android.core.lang.CSHasId
import renetik.android.event.common.CSHasDestruct
import renetik.android.event.common.CSModel
import renetik.android.event.common.destruct
import renetik.android.event.common.onDestructed
import renetik.android.preset.CSHasPresetIdHasId
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
    private val parent: CSHasDestruct,
    override val id: String,
    override val preset: Preset,
    override val presetId: String,
    override val key: String = presetId
) : CSModel(parent), StoreContext, CSHasPresetIdHasId {

    companion object {
        fun PresetStoreContext(
            parent: CSHasDestruct, hasPresetHasId: CSHasPresetIdHasId, key: String
        ) = PresetStoreContext(
            parent, id = "${hasPresetHasId.id} $key", preset = hasPresetHasId.preset,
            presetId = "${hasPresetHasId.presetId} $key", key
        )

        fun PresetStoreContext(
            parent: CSHasDestruct, hasId: CSHasId, preset: Preset, key: String,
        ) = PresetStoreContext(
            parent, id = "${hasId.id} $key", preset = preset, presetId = key, key
        )
    }

    private val childContexts = mutableListOf<StoreContext>()

    private fun <T : StoreContext> T.init() = apply {
        childContexts += this; onDestructed { childContexts -= this }
    }

    override fun context(parent: CSHasDestruct, key: String?): PresetStoreContext =
        (key?.let { PresetStoreContext(parent, hasPresetHasId = this, key) }
            ?: PresetStoreContext(parent, id, preset, presetId, this.key)).init()

    override fun appContext(parent: CSHasDestruct, key: String) =
        AppStoreContext(parent, hasId = this, key).init()

    override fun memoryContext(parent: CSHasDestruct, key: String) =
        RuntimeStoreContext(parent, this, key).init()

    override fun onChange(function: (Unit) -> Unit) = preset.onChange(function)

    private val keys = mutableListOf<String>()

    private val properties = mutableListOf<CSPresetProperty<*>>()
    private fun <T : CSPresetProperty<*>> T.init() = apply {
        properties += this; onDestructed { properties -= this }
    }

    private val presets = mutableListOf<CSPreset<*, *>>()

    fun add(preset: CSPreset<*, *>) {
        presets += preset; preset.onDestructed { presets -= preset }
    }

    override fun clear() = preset.store.operation {
        properties.forEach(CSPresetProperty<*>::clear)
        childContexts.onEach(StoreContext::clear)
        presets.onEach(CSPreset<*, *>::clear)
    }

    override fun destructClear() {
        clear()
        parent.destruct()
    }

    override fun property(
        key: String, default: String, onChange: ArgFunc<String>?,
    ) = preset.property(
        this, keys.put("$presetId $key"), default, onChange
    ).init()

    override fun property(
        key: String, default: Boolean, onChange: ArgFunc<Boolean>?,
    ) = preset.property(
        this, keys.put("$presetId $key"), default, onChange
    ).init()

    override fun property(
        key: String, default: Float, onChange: ArgFunc<Float>?,
    ) = preset.property(
        this, keys.put("$presetId $key"), default, onChange
    ).init()

    override fun property(
        key: String, default: Int, onChange: ArgFunc<Int>?
    ) = preset.property(
        this, keys.put("$presetId $key"), default, onChange
    ).init()

    override fun <T> property(
        key: String, values: () -> List<T>, default: () -> T, onChange: ArgFunc<T>?
    ) = preset.property(
        this, keys.put("$presetId $key"), values, default, onChange
    ).init()

    override fun nullIntProperty(
        key: String, default: Int?, onChange: ((value: Int?) -> Unit)?
    ) = preset.nullIntProperty(
        this, keys.put("$presetId $key"), default, onChange
    ).init()

    override fun nullFloatProperty(
        key: String, default: Float?, onChange: ((value: Float?) -> Unit)?
    ) = preset.nullFloatProperty(
        this, keys.put("$presetId $key"), default, onChange
    ).init()

    override fun nullStringProperty(
        key: String, default: String?, onChange: ((value: String?) -> Unit)?
    ) = preset.nullStringProperty(
        this, keys.put("$presetId $key"), default, onChange
    ).init()

    override fun <T> nullListItemProperty(
        key: String, values: List<T>, default: T?, onChange: ((value: T?) -> Unit)?
    ) = preset.nullListItemProperty(
        this, keys.put("$presetId $key"), values, default, onChange
    ).init()

    override fun property(
        key: String, default: List<Int>, onChange: ArgFunc<List<Int>>?
    ) = preset.property(
        this, keys.put("$presetId $key"), default, onChange
    ).init()
}