package renetik.android.preset.context

import renetik.android.core.lang.ArgFunc

fun <T> PresetStoreContext.property(
    key: String, getValues: () -> List<T>,
    defaultIndex: Int, onChange: ArgFunc<T>? = null
) = property(key, getValues, { getValues()[defaultIndex] }, onChange)

fun <T> PresetStoreContext.property(
    key: String, getValues: () -> List<T>, default: T, onChange: ArgFunc<T>? = null
) = property(key, getValues, { default }, onChange)

fun <T> PresetStoreContext.property(
    key: String, values: List<T>, defaultIndex: Int, onChange: ArgFunc<T>? = null
) = property(key, { values }, values[defaultIndex], onChange)

fun <T> PresetStoreContext.property(
    key: String, values: List<T>, default: T, onChange: ArgFunc<T>? = null
) = property(key, { values }, { default }, onChange)

fun <T> PresetStoreContext.property(
    key: String, values: List<T>, default: () -> T, onChange: ArgFunc<T>? = null
) = property(key, { values }, default, onChange)