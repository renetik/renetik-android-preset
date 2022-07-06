package renetik.android.preset.property.nullable

import renetik.android.event.owner.CSEventOwnerHasDestroy
import renetik.android.event.property.CSListValuesEventProperty
import renetik.android.preset.CSPreset
import renetik.android.store.CSStore
import renetik.android.core.kotlin.toId
import renetik.android.json.obj.getValue

class CSListItemNullablePresetProperty<T>(
    parent: CSEventOwnerHasDestroy,
    preset: CSPreset<*, *>,
    key: String,
    val getValues: () -> List<T>,
    val getDefault: () -> T,
    onChange: ((value: T?) -> Unit)? = null)
    : CSNullablePresetProperty<T>(parent, preset, key, onChange),
    CSListValuesEventProperty<T?> {

    constructor(
        parent: CSEventOwnerHasDestroy,
        preset: CSPreset<*, *>, key: String,
        values: List<T>, default: T, onChange: ((value: T?) -> Unit)? = null
    ) : this(parent, preset, key, getValues = { values },
        getDefault = { default }, onChange)

    override val values: List<T> get() = getValues()
    override val default: T get() = getDefault()
    override var _value = load()
    override fun get(store: CSStore) = store.getValue(key, values)
    override fun set(store: CSStore, value: T?) {
        store.set(key, value.toId())
    }
}