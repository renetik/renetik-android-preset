package renetik.android.preset.property

import renetik.android.event.property.CSEventProperty
import renetik.android.event.property.connect
import renetik.android.event.owner.register
import renetik.android.store.CSStore

val CSPresetProperty<*>.store: CSStore get() = preset.store

fun <T> CSPresetProperty<T>.followPresetIf(property: CSEventProperty<Boolean>) = apply {
    parent.register(isFollowPreset.connect(property))
}