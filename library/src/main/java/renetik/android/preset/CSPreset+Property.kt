package renetik.android.preset

import renetik.android.core.lang.CSHasId
import renetik.android.event.registrations.CSHasRegistrationsHasDestroy
import renetik.android.preset.property.nullable.CSFloatNullablePresetProperty
import renetik.android.preset.property.nullable.CSIntNullablePresetProperty
import renetik.android.preset.property.nullable.CSListItemNullablePresetProperty
import renetik.android.preset.property.nullable.CSStringNullablePresetProperty
import renetik.android.preset.property.value.*
import renetik.android.store.type.CSJsonObjectStore
import kotlin.reflect.KClass

fun CSPreset<*, *>.property(
    parent: CSHasRegistrationsHasDestroy, key: String, default: String,
    onChange: ((value: String) -> Unit)? = null
) = add(CSStringValuePresetProperty(parent, this, key, default, onChange))

fun CSPreset<*, *>.property(
    parent: CSHasRegistrationsHasDestroy, key: String, default: Boolean,
    onChange: ((value: Boolean) -> Unit)? = null
) = add(CSBooleanValuePresetProperty(parent, this, key, default, onChange))

fun CSPreset<*, *>.property(
    parent: CSHasRegistrationsHasDestroy, key: String, default: Int,
    onChange: ((value: Int) -> Unit)? = null
) = add(CSIntValuePresetProperty(parent, this, key, default, onChange))

fun CSPreset<*, *>.property(
    parent: CSHasRegistrationsHasDestroy, key: String, default: Float,
    onChange: ((value: Float) -> Unit)? = null
) = add(CSFloatValuePresetProperty(parent, this, key, default, onChange))

fun CSPreset<*, *>.property(
    parent: CSHasRegistrationsHasDestroy, key: String, default: Double,
    onChange: ((value: Double) -> Unit)? = null
) = add(CSDoubleValuePresetProperty(parent, this, key, default, onChange))

fun <T> CSPreset<*, *>.property(
    parent: CSHasRegistrationsHasDestroy, key: String, values: List<T>, default: T,
    onChange: ((value: T) -> Unit)? = null
) = add(CSListItemValuePresetProperty(parent, this, key, values, default, onChange))

fun <T> CSPreset<*, *>.property(
    parent: CSHasRegistrationsHasDestroy, key: String, values: List<T>, getDefault: () -> T,
    onChange: ((value: T) -> Unit)? = null
) = add(CSListItemValuePresetProperty(parent, this, key,
    { values }, getDefault, onChange))

fun <T> CSPreset<*, *>.property(
    parent: CSHasRegistrationsHasDestroy, key: String, getValues: () -> Collection<T>,
    getDefault: () -> T, onChange: ((value: T) -> Unit)? = null
) = add(CSListItemValuePresetProperty(parent, this, key,
    getValues, getDefault, onChange))

fun <T> CSPreset<*, *>.property(
    parent: CSHasRegistrationsHasDestroy, key: String, getValues: () -> List<T>,
    defaultIndex: Int, onChange: ((value: T) -> Unit)? = null
) = add(CSListItemValuePresetProperty(parent, this, key, getValues,
    { getValues()[defaultIndex] }, onChange))

fun <T> CSPreset<*, *>.property(
    parent: CSHasRegistrationsHasDestroy, key: String, values: List<T>, defaultIndex: Int,
    onChange: ((value: T) -> Unit)? = null
) = property(parent, key, values, values[defaultIndex], onChange)

fun <T> CSPreset<*, *>.property(
    parent: CSHasRegistrationsHasDestroy, key: String, values: Array<T>, default: T,
    onChange: ((value: T) -> Unit)? = null
) = property(parent, key, values.asList(), default, onChange)

fun <T> CSPreset<*, *>.property(
    parent: CSHasRegistrationsHasDestroy, key: String, values: Array<T>, defaultIndex: Int,
    onChange: ((value: T) -> Unit)? = null
) = property(parent, key, values.asList(), values[defaultIndex], onChange)

fun <T : CSHasId> CSPreset<*, *>.property(
    parent: CSHasRegistrationsHasDestroy, key: String, values: List<T>, default: List<T>,
    onChange: ((value: List<T>) -> Unit)? = null
) = add(CSListValuePresetProperty(parent, this, key, values, default, onChange))

fun <T : CSHasId> CSPreset<*, *>.property(
    parent: CSHasRegistrationsHasDestroy, key: String, values: Array<T>, default: List<T>,
    onChange: ((value: List<T>) -> Unit)? = null
) = property(parent, key, values.asList(), default, onChange)

fun <T : CSJsonObjectStore> CSPreset<*, *>.property(
    parent: CSHasRegistrationsHasDestroy, key: String, listType: KClass<T>,
    default: List<T> = emptyList(),
    onApply: ((value: List<T>) -> Unit)? = null
) = add(CSJsonListValuePresetProperty(parent, this,
    key, listType, default, onApply))

fun <T : CSJsonObjectStore> CSPreset<*, *>.property(
    parent: CSHasRegistrationsHasDestroy, key: String, type: KClass<T>,
    onApply: ((value: T) -> Unit)? = null
) = add(CSJsonTypeValuePresetProperty(parent, this, key, type, onApply))

fun CSPreset<*, *>.propertyNullString(
    parent: CSHasRegistrationsHasDestroy, key: String, default: String? = null,
    onChange: ((value: String?) -> Unit)? = null
) = add(CSStringNullablePresetProperty(parent, this, key, default, onChange))

fun CSPreset<*, *>.propertyNullInt(
    parent: CSHasRegistrationsHasDestroy, key: String, default: Int? = null,
    onChange: ((value: Int?) -> Unit)? = null
) = add(CSIntNullablePresetProperty(parent, this, key, default, onChange))

fun CSPreset<*, *>.propertyNullFloat(
    parent: CSHasRegistrationsHasDestroy, key: String, default: Float? = null,
    onChange: ((value: Float?) -> Unit)? = null
) = add(CSFloatNullablePresetProperty(parent, this, key, default, onChange))

fun <T> CSPreset<*, *>.propertyNullListItem(
    parent: CSHasRegistrationsHasDestroy, key: String, values: List<T>, default: T? = null,
    onChange: ((value: T?) -> Unit)? = null
) = add(CSListItemNullablePresetProperty(parent, this,
    key, values, default, onChange))

fun <T> CSPreset<*, *>.propertyNullListItem(
    parent: CSHasRegistrationsHasDestroy, key: String, getValues: () -> List<T>, default: T? = null,
    onChange: ((value: T?) -> Unit)? = null
) = add(CSListItemNullablePresetProperty(parent, this,
    key, getValues, { default }, onChange))

fun <T> CSPreset<*, *>.propertyNullArrayItem(
    parent: CSHasRegistrationsHasDestroy, key: String, values: Array<T>, default: T? = null,
    onChange: ((value: T?) -> Unit)? = null
) = propertyNullListItem(parent, key, values.asList(), default, onChange)