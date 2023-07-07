package renetik.android.preset.extensions

import renetik.android.core.lang.Func
import renetik.android.event.property.action
import renetik.android.event.registration.CSHasChange
import renetik.android.event.registration.CSHasChangeValue
import renetik.android.event.registration.CSRegistration
import renetik.android.preset.Preset

inline fun CSHasChange<*>.onChange(
    preset: Preset, crossinline function: Func
): CSRegistration {
    val registration = onChange { function() }
    return CSRegistration(
        registration,
        preset.onBeforeChange { registration.pause() },
        preset.onChange {
            function()
            registration.resume()
        }
    )
}

inline fun <T, V : CSHasChangeValue<T>> V.action(
    preset: Preset, crossinline function: (T) -> Unit
): CSRegistration {
    val registration = action { function(it) }
    return CSRegistration(
        registration,
        preset.onBeforeChange { registration.pause() },
        preset.onChange {
            function(value)
            registration.resume()
        }
    )
}