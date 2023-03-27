package renetik.android.preset.extensions

import renetik.android.core.lang.ArgFunc
import renetik.android.event.common.CSHasRegistrationsHasDestruct
import renetik.android.preset.Preset
import renetik.android.preset.property.CSPresetProperty
import renetik.android.preset.property.value.CSJsonListValuePresetProperty
import renetik.android.preset.property.value.CSJsonMutableListValuePresetProperty
import renetik.android.preset.property.value.CSJsonValuePresetProperty
import renetik.android.store.type.CSJsonObjectStore
import kotlin.reflect.KClass

fun <T : CSJsonObjectStore> Preset.property(
    parent: CSHasRegistrationsHasDestruct, key: String, listType: KClass<T>,
    default: List<T> = emptyList(),
    onChange: ArgFunc<List<T>>? = null): CSPresetProperty<List<T>> =
    add(CSJsonListValuePresetProperty(parent, this, key, listType, default, onChange))

inline fun <reified T : CSJsonObjectStore> Preset.property(
    parent: CSHasRegistrationsHasDestruct, key: String, default: List<T> = emptyList(),
    noinline onChange: ArgFunc<List<T>>? = null): CSPresetProperty<List<T>> =
    property(parent, key, T::class, default, onChange)

@JvmName("propertyMutableList")
fun <T : CSJsonObjectStore> Preset.property(
    parent: CSHasRegistrationsHasDestruct, key: String, listType: KClass<T>,
    default: MutableList<T> = mutableListOf(),
    onChange: ArgFunc<MutableList<T>>? = null): CSPresetProperty<MutableList<T>> =
    add(CSJsonMutableListValuePresetProperty(parent, this, key,
        listType, default, onChange))

@JvmName("propertyMutableList")
inline fun <reified T : CSJsonObjectStore> Preset.property(
    parent: CSHasRegistrationsHasDestruct, key: String,
    default: MutableList<T> = mutableListOf(),
    noinline onChange: ArgFunc<MutableList<T>>? = null)
        : CSPresetProperty<MutableList<T>> =
    property(parent, key, T::class, default, onChange)

fun <T : CSJsonObjectStore> Preset.property(
    parent: CSHasRegistrationsHasDestruct, key: String, type: KClass<T>,
    onChange: ArgFunc<T>? = null): CSPresetProperty<T> =
    add(CSJsonValuePresetProperty(parent, this, key, type, onChange))

inline fun <reified T : CSJsonObjectStore> Preset.property(
    parent: CSHasRegistrationsHasDestruct, key: String,
    noinline onChange: ArgFunc<T>? = null): CSPresetProperty<T> =
    add(CSJsonValuePresetProperty(parent, this, key, T::class, onChange))

