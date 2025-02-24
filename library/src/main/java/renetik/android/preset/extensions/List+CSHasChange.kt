package renetik.android.preset.extensions

import renetik.android.core.lang.Func
import renetik.android.event.common.Debouncer.Companion.debouncer
import renetik.android.event.registration.CSHasChange
import renetik.android.event.registration.CSRegistration
import renetik.android.event.registration.CSRegistrationsMap
import renetik.android.preset.Preset

//inline fun <T : CSHasChange<*>> List<T>.onChange(
//    preset: Preset, crossinline function: Func
//): CSRegistration {
//    val registrations = CSRegistrationsList(this)
//    forEach { registrations.register(it.onChange { function() }) }
//    return CSRegistration(
//        registrations,
//        preset.onBeforeChange { registrations.pause() },
//        preset.onChange {
//            function()
//            registrations.resume()
//        }
//    )
//}

inline fun <T : CSHasChange<*>> List<T>.onChangeLater(
    preset: Preset, crossinline function: Func
): CSRegistration {
    val registrations = CSRegistrationsMap(this)
    val laterOnceFunction = registrations.debouncer { function() }
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