package renetik.android.preset.property

import renetik.android.event.property.computedFrom
import renetik.android.store.property.CSStoreProperty

fun <T : CSStoreProperty<Int>> T.max(value: Int) =
    computedFrom(from = { it.coerceAtMost(value) })
//    apply { filter = { it?.coerceAtMost(value) } }

fun <T : CSStoreProperty<Float?>> T.max(value: Float?) =
    computedFrom(from = { from ->
        value?.let { from?.coerceAtMost(value) } ?: from
    })
//    apply {
//    value?.let { filter = { it?.coerceAtMost(value) } }
//}

fun <T : CSStoreProperty<Int>> T.min(value: Int) =
    computedFrom(from = { it.coerceAtLeast(value) })
//    apply {
//    filter = { it?.coerceAtLeast(value) }
//}

fun <T : CSStoreProperty<Float?>> T.min(value: Float?) =
    computedFrom(from = { from ->
        value?.let { from?.coerceAtLeast(value) } ?: from
    })
//    apply {
//    value?.let { filter = { it?.coerceAtLeast(value) } }
//}