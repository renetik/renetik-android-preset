package renetik.android.preset.property

import renetik.android.event.common.CSHasRegistrationsHasDestroy
import renetik.android.event.property.CSProperty

interface CSPresetProperty<T> : CSProperty<T>, CSPresetKeyData {
    val parent: CSHasRegistrationsHasDestroy
    val isFollowPreset: CSProperty<Boolean>
    val isModified: Boolean
}

