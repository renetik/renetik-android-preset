package renetik.android.preset

import renetik.android.core.lang.Func
import renetik.android.event.property.CSProperty
import renetik.android.event.property.connect
import renetik.android.event.registration.CSRegistration
import renetik.android.event.registration.pause
import renetik.android.event.registration.register
import renetik.android.event.registration.resume

typealias Preset = CSPreset<*, out CSPresetDataList<*>>

fun <T : Preset> T.followStoreIf(property: CSProperty<Boolean>) = apply {
    register(isFollowStore.connect(property))
}

fun <T : Preset> T.onChange(
    vararg registrations: CSRegistration,
    function: Func
): CSRegistration {
    val onBeforeChange = onBeforeChange { registrations.pause() }
    val onChange = onChange {
        function()
        registrations.resume()
    }
    return CSRegistration(onBeforeChange, onChange)
}