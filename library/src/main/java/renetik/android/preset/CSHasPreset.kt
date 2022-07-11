package renetik.android.preset

import renetik.android.event.common.CSHasRegistrationsHasDestroy

interface CSHasPreset : CSHasRegistrationsHasDestroy {
	val preset: Preset
	val presetId: String
}