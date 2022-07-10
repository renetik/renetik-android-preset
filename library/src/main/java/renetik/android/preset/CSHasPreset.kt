package renetik.android.preset

import renetik.android.event.registrations.CSHasRegistrationsHasDestroy

interface CSHasPreset : CSHasRegistrationsHasDestroy {
	val preset: Preset
	val presetId: String
}