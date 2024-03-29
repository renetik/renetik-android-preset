package renetik.android.preset.property.value

import renetik.android.core.kotlin.collections.reload
import renetik.android.core.lang.ArgFunc
import renetik.android.event.common.CSHasDestruct
import renetik.android.preset.CSPreset
import renetik.android.store.CSStore
import renetik.android.store.type.CSJsonObjectStore
import kotlin.reflect.KClass

class CSJsonMutableListValuePresetProperty<T : CSJsonObjectStore>(
    parent: CSHasDestruct,
    preset: CSPreset<*, *>,
    key: String,
    val type: KClass<T>,
    override val default: MutableList<T> = mutableListOf(),
    onChange: ArgFunc<MutableList<T>>?
) : CSValuePresetProperty<MutableList<T>>(parent, preset, key, onChange) {

    override fun get(store: CSStore) = store.getJsonObjectList(key, type)
        ?.let { mutableListOf<T>().reload(it) } ?: default

    override fun set(store: CSStore, value: MutableList<T>) = store.setJsonObjectList(key, value)
}