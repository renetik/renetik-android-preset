package renetik.android.preset.property

import renetik.android.core.kotlin.unexpected
import renetik.android.event.property.CSProperty
import renetik.android.store.CSStore
import renetik.android.store.property.CSStoreProperty

interface CSPresetProperty<T> : CSStoreProperty<T>, CSPresetKeyData {
    val default: T
    override val store get() = preset.store
    val isFollowPreset: CSProperty<Boolean>
    override fun listenStoreLoad() = unexpected()
    override fun clearKeyData() = super.clear()
}

fun <T : CSPresetProperty<*>> T.isModifiedIn(store: CSStore) = if (store.has(key))
    value != getFiltered(store) else value != default

fun <T : CSPresetProperty<*>> T.isModified() = isModifiedIn(store)



