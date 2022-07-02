package renetik.android.preset.property.value

import renetik.android.core.kotlin.toId
import renetik.android.event.owner.CSEventOwnerHasDestroy
import renetik.android.event.property.CSListValuesEventProperty
import renetik.android.json.obj.getValue
import renetik.android.preset.CSPreset
import renetik.android.store.CSStore

open class CSListItemValuePresetEventProperty<T>(
    parent: CSEventOwnerHasDestroy,
    preset: CSPreset<*, *>,
    key: String,
    val getValues: () -> Collection<T>,
    val getDefault: () -> T,
    onChange: ((value: T) -> Unit)? = null,
) : CSValuePresetEventProperty<T>(parent, preset, key, onChange), CSListValuesEventProperty<T> {

    constructor(
        parent: CSEventOwnerHasDestroy,
        preset: CSPreset<*, *>, key: String,
        values: Collection<T>, default: T, onChange: ((value: T) -> Unit)? = null,
    ) : this(parent, preset, key, getValues = { values },
        getDefault = { default }, onChange)

    override val values: List<T> get() = getValues().toList()
    override val default: T get() = getDefault()
    override var _value: T = load()
    override fun get(store: CSStore): T? = store.getValue(key, values)
    override fun set(store: CSStore, value: T) = store.set(key, value.toId())
}