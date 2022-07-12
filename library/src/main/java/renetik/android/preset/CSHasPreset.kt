package renetik.android.preset

import renetik.android.event.common.CSHasRegistrationsHasDestroy

interface CSHasPreset : CSHasRegistrationsHasDestroy {
    companion object

    val preset: Preset
    val presetId: String
}