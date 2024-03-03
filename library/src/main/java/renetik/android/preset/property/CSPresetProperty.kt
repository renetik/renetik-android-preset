package renetik.android.preset.property

import renetik.android.event.property.CSProperty

interface CSPresetProperty<T> : CSProperty<T>, CSPresetKeyData {
    companion object

    val isFollowPreset: CSProperty<Boolean>
    var filter: ((T?) -> T?)?
}

