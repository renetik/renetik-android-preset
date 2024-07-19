package renetik.android.preset.property

import renetik.android.core.kotlin.unexpected
import renetik.android.event.property.CSProperty
import renetik.android.store.property.CSStoreProperty

interface CSPresetProperty<T> : CSStoreProperty<T>, CSPresetKeyData {
    companion object;
    override val store get() = preset.store
    val isFollowPreset: CSProperty<Boolean>
    override fun listenStoreLoad() = unexpected()
}

