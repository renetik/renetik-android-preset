package renetik.android.preset.property

import renetik.android.event.property.CSProperty
import renetik.android.event.property.connect
import renetik.android.event.registrations.register
import renetik.android.store.CSStore

val CSPresetProperty<*>.store: CSStore get() = preset.store

fun <T> CSPresetProperty<T>.followPresetIf(property: CSProperty<Boolean>) = apply {
    parent.register(isFollowPreset.connect(property))
}