package renetik.android.preset

import renetik.android.event.owner.CSEventOwnerHasDestroy

interface CSHasPreset : CSEventOwnerHasDestroy {
	val preset: Preset
	val presetId: String
}