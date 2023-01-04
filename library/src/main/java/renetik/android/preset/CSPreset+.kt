package renetik.android.preset

import renetik.android.event.property.CSProperty
import renetik.android.event.property.connect
import renetik.android.event.registration.register

typealias Preset = CSPreset<*, out CSPresetDataList<*>>

fun <T : Preset> T.followStoreIf(property: CSProperty<Boolean>) = apply {
    register(isFollowStore.connect(property))
}