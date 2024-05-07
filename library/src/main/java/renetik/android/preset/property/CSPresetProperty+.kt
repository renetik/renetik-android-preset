package renetik.android.preset.property

import renetik.android.store.property.CSStoreProperty

fun <T : CSStoreProperty<Int>> T.max(value: Int) = apply {
    filter = { it?.coerceAtMost(value) }
}

fun <T : CSStoreProperty<Int>> T.min(value: Int) = apply {
    filter = { it?.coerceAtLeast(value) }
}