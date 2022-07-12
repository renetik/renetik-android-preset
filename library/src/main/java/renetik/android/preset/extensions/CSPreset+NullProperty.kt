package renetik.android.preset.extensions

import renetik.android.core.lang.ArgFunc
import renetik.android.event.common.CSHasRegistrationsHasDestroy
import renetik.android.preset.Preset
import renetik.android.preset.property.nullable.CSBooleanNullablePresetProperty
import renetik.android.preset.property.nullable.CSFloatNullablePresetProperty
import renetik.android.preset.property.nullable.CSIntNullablePresetProperty
import renetik.android.preset.property.nullable.CSStringNullablePresetProperty

fun Preset.nullStringProperty(
    parent: CSHasRegistrationsHasDestroy, key: String, default: String? = null,
    onChange: ((value: String?) -> Unit)? = null
) = add(CSStringNullablePresetProperty(parent, this, key, default, onChange))

fun Preset.nullBoolProperty(
    parent: CSHasRegistrationsHasDestroy, key: String, default: Boolean? = null,
    onChange: ArgFunc<Boolean?>? = null
) = add(CSBooleanNullablePresetProperty(parent, this, key, default, onChange))

fun Preset.nullIntProperty(
    parent: CSHasRegistrationsHasDestroy, key: String, default: Int? = null,
    onChange: ((value: Int?) -> Unit)? = null
) = add(CSIntNullablePresetProperty(parent, this, key, default, onChange))

fun Preset.nullFloatProperty(
    parent: CSHasRegistrationsHasDestroy, key: String, default: Float? = null,
    onChange: ((value: Float?) -> Unit)? = null
) = add(CSFloatNullablePresetProperty(parent, this, key, default, onChange))

