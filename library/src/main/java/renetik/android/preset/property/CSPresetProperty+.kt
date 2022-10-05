package renetik.android.preset.property

fun <T : CSPresetProperty<Int>> T.max(value: Int) = apply {
    filter = { it?.coerceAtMost(value) }
}

fun <T : CSPresetProperty<Int>> T.min(value: Int) = apply {
    filter = { it?.coerceAtLeast(value) }
}