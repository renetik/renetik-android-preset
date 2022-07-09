package renetik.android.preset

import renetik.android.event.property.CSEventProperty
import renetik.android.event.property.connect
import renetik.android.event.owner.register

typealias Preset = CSPreset<*, out CSPresetItemList<*>>

fun Preset.followStoreIf(property: CSEventProperty<Boolean>) {
	register(isFollowStore.connect(property))
}