package renetik.android.preset.extensions

import renetik.android.core.lang.Func
import renetik.android.event.common.CSLaterOnceFunc.Companion.laterOnce
import renetik.android.event.registration.CSHasChange
import renetik.android.event.registration.CSRegistration
import renetik.android.event.registration.CSRegistrationsList
import renetik.android.preset.Preset

inline fun <T : CSHasChange<*>> List<T>.onChange(
    preset: Preset, crossinline function: Func
): CSRegistration {
    val registrations = CSRegistrationsList(this)
    forEach { registrations.register(it.onChange { function() }) }
    return CSRegistration(
        registrations,
        preset.onBeforeChange { registrations.pause() },
        preset.onChange {
            function()
            registrations.resume()
        }
    )
}

inline fun <T : CSHasChange<*>> List<T>.onChangeLater(
    preset: Preset, crossinline function: Func
): CSRegistration {
    val registrations = CSRegistrationsList(this)
    val laterOnceFunction = registrations.laterOnce { function() }
    forEach { registrations.register(it.onChange { laterOnceFunction() }) }
    return CSRegistration(
        registrations,
        preset.onBeforeChange { registrations.pause() },
        preset.onChange {
            function()
            registrations.resume()
        }
    )
}