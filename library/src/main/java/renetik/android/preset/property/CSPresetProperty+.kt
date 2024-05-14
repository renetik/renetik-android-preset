package renetik.android.preset.property

import renetik.android.store.property.CSStoreProperty

fun <T : CSStoreProperty<Int>> T.max(value: Int) = apply {
    filter = { it?.coerceAtMost(value) }
}

fun <T : CSStoreProperty<Float?>> T.max(value: Float?) = apply {
    value?.let { filter = { it?.coerceAtMost(value) } }
}

fun <T : CSStoreProperty<Int>> T.min(value: Int) = apply {
    filter = { it?.coerceAtLeast(value) }
}

fun <T : CSStoreProperty<Float?>> T.min(value: Float?) = apply {
    value?.let { filter = { it?.coerceAtLeast(value) } }
}