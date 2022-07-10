package renetik.android.preset

import renetik.android.event.property.CSProperty
import renetik.android.event.property.connect
import renetik.android.event.registrations.register

typealias Preset = CSPreset<*, out CSPresetItemList<*>>

fun Preset.followStoreIf(property: CSProperty<Boolean>) {
	register(isFollowStore.connect(property))
}