package renetik.android.preset.extensions

import renetik.android.event.common.CSHasRegistrationsHasDestruct
import renetik.android.preset.Preset
import renetik.android.preset.property.nullable.CSListItemNullablePresetProperty

fun <T> Preset.nullListItemProperty(
    parent: CSHasRegistrationsHasDestruct, key: String, values: List<T>,
    default: T? = null, onChange: ((value: T?) -> Unit)? = null
) = add(CSListItemNullablePresetProperty(parent, this,
    key, values, default, onChange))

fun <T> Preset.nullListItemProperty(
    parent: CSHasRegistrationsHasDestruct, key: String, getValues: () -> List<T>,
    default: T? = null, onChange: ((value: T?) -> Unit)? = null
) = add(CSListItemNullablePresetProperty(parent, this,
    key, getValues, { default }, onChange))

fun <T> Preset.nullArrayItemProperty(
    parent: CSHasRegistrationsHasDestruct, key: String, values: Array<T>,
    default: T? = null, onChange: ((value: T?) -> Unit)? = null
) = nullListItemProperty(parent, key, values.asList(), default, onChange)