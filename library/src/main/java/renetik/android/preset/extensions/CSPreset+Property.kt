package renetik.android.preset.extensions

import renetik.android.core.lang.ArgFunc
import renetik.android.core.lang.CSHasId
import renetik.android.event.common.CSHasRegistrationsHasDestroy
import renetik.android.preset.CSPreset
import renetik.android.preset.Preset
import renetik.android.preset.property.value.*

fun CSPreset<*, *>.property(
    parent: CSHasRegistrationsHasDestroy, key: String,
    default: String, onChange: ArgFunc<String>? = null) =
    add(CSStringValuePresetProperty(parent, this, key, default, onChange))

fun Preset.property(
    parent: CSHasRegistrationsHasDestroy, key: String,
    default: Boolean, onChange: ArgFunc<Boolean>? = null) =
    add(CSBooleanValuePresetProperty(parent, this, key, default, onChange))

fun Preset.property(
    parent: CSHasRegistrationsHasDestroy, key: String,
    default: Int, onChange: ArgFunc<Int>? = null) =
    add(CSIntValuePresetProperty(parent, this, key, default, onChange))

fun Preset.property(
    parent: CSHasRegistrationsHasDestroy, key: String,
    default: Float, onChange: ArgFunc<Float>? = null) =
    add(CSFloatValuePresetProperty(parent, this, key, default, onChange))

fun Preset.property(
    parent: CSHasRegistrationsHasDestroy, key: String,
    default: Double, onChange: ArgFunc<Double>? = null) =
    add(CSDoubleValuePresetProperty(parent, this, key, default, onChange))

fun Preset.property(
    parent: CSHasRegistrationsHasDestroy, key: String,
    default: List<Int>, onChange: ArgFunc<List<Int>>? = null) =
    add(CSIntListValuePresetProperty(parent, this, key, default, onChange))

fun <T> Preset.property(
    parent: CSHasRegistrationsHasDestroy, key: String, values: List<T>,
    default: T, onChange: ArgFunc<T>? = null) =
    add(CSListItemValuePresetProperty(parent, this, key, values, default, onChange))

fun <T> Preset.property(
    parent: CSHasRegistrationsHasDestroy, key: String, values: List<T>,
    getDefault: () -> T, onChange: ArgFunc<T>? = null) =
    add(CSListItemValuePresetProperty(parent, this, key,
        { values }, getDefault, onChange))

fun <T> Preset.property(
    parent: CSHasRegistrationsHasDestroy, key: String, values: () -> Collection<T>,
    getDefault: () -> T, onChange: ArgFunc<T>? = null) =
    add(CSListItemValuePresetProperty(parent, this, key,
        values, getDefault, onChange))

fun <T> Preset.property(
    parent: CSHasRegistrationsHasDestroy, key: String, getValues: () -> List<T>,
    defaultIndex: Int, onChange: ArgFunc<T>? = null) =
    add(CSListItemValuePresetProperty(parent, this, key, getValues,
        { getValues()[defaultIndex] }, onChange))

fun <T> Preset.property(
    parent: CSHasRegistrationsHasDestroy, key: String, getValues: () -> List<T>,
    default: T, onChange: ArgFunc<T>? = null) =
    add(CSListItemValuePresetProperty(parent, this, key, getValues,
        { default }, onChange))

fun <T> Preset.property(
    parent: CSHasRegistrationsHasDestroy, key: String, values: List<T>,
    defaultIndex: Int, onChange: ArgFunc<T>? = null) =
    property(parent, key, values, values[defaultIndex], onChange)

fun <T> Preset.property(
    parent: CSHasRegistrationsHasDestroy, key: String, values: Array<T>,
    default: T, onChange: ArgFunc<T>? = null) =
    property(parent, key, values.asList(), default, onChange)

fun <T> Preset.property(
    parent: CSHasRegistrationsHasDestroy, key: String, values: Array<T>,
    defaultIndex: Int, onChange: ArgFunc<T>? = null) =
    property(parent, key, values.asList(), values[defaultIndex], onChange)

fun <T : CSHasId> Preset.property(
    parent: CSHasRegistrationsHasDestroy, key: String, values: List<T>,
    default: List<T>, onChange: ArgFunc<List<T>>? = null) =
    add(CSListValuePresetProperty(parent, this, key, values, default, onChange))

fun <T : CSHasId> Preset.property(
    parent: CSHasRegistrationsHasDestroy, key: String, values: Array<T>,
    default: List<T>, onChange: ArgFunc<List<T>>? = null) =
    property(parent, key, values.asList(), default, onChange)