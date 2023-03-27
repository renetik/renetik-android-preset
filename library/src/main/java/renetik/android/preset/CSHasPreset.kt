package renetik.android.preset

import renetik.android.event.common.CSHasRegistrationsHasDestruct

interface CSHasPreset : CSHasRegistrationsHasDestruct {
    companion object

    val preset: Preset
    val presetId: String
}