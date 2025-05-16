package renetik.android.preset.property.value

import renetik.android.event.common.CSHasDestruct
import renetik.android.preset.CSPreset
import renetik.android.store.CSStore

class CSIntValuePresetProperty(
    parent: CSHasDestruct, preset: CSPreset<*, *>, key: String,
    private val getDefault: () -> Int, onChange: ((value: Int) -> Unit)?
) : CSValuePresetProperty<Int>(parent, preset, key, onChange) {

    constructor(
        parent: CSHasDestruct, preset: CSPreset<*, *>, key: String,
        default: Int, onChange: ((value: Int) -> Unit)?
    ) : this(parent, preset, key, { default }, onChange)

    override val default: Int get() = getDefault()

    override fun get(store: CSStore) =
        store.getInt(key)

    override fun set(store: CSStore, value: Int) =
        store.set(key, value)
}


