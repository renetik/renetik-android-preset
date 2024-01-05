package renetik.android.preset

import renetik.android.core.lang.ArgFunc
import renetik.android.core.lang.Func
import renetik.android.event.property.CSProperty
import renetik.android.event.property.connect
import renetik.android.event.registration.CSHasChange
import renetik.android.event.registration.CSHasChange.Companion.action
import renetik.android.event.registration.CSHasChangeValue
import renetik.android.event.registration.CSHasRegistrations
import renetik.android.event.registration.CSRegistration
import renetik.android.event.registration.pause
import renetik.android.event.registration.plus
import renetik.android.event.registration.resume

fun <T : Preset> T.followStoreIf(property: CSProperty<Boolean>) =
    apply { this + isFollowStore.connect(property) }

fun <T : Preset> T.onChange(
    before: Func,
    onChange: Func
): CSRegistration = CSRegistration(
    onBeforeChange { before() },
    onChange { onChange() }
)

fun <T : Preset> T.onChange(
    vararg registrations: CSRegistration,
    onChange: Func
): CSRegistration = CSRegistration(
    onBeforeChange { registrations.pause() },
    onChange {
        onChange()
        registrations.resume()
    }
)

fun <T : Preset> T.onChange(
    parent: CSHasRegistrations,
    hasChange: CSHasChange<*>,
    onChange: Func
): CSRegistration = onChange(
    parent + hasChange.onChange { onChange() },
    onChange = { onChange() }
)

fun <T : Preset> T.action(
    parent: CSHasRegistrations,
    hasChange: CSHasChange<*>,
    onChange: Func
): CSRegistration = onChange(
    parent + hasChange.action { onChange() },
    onChange = { onChange() }
)

fun <T : Preset, V> T.onChange(
    parent: CSHasRegistrations,
    property: CSHasChangeValue<V>,
    onChange: ArgFunc<V>
): CSRegistration = onChange(
    parent + property.onChange { onChange(property.value) },
    onChange = { onChange(property.value) }
)

fun <T : Preset, V> T.action(
    parent: CSHasRegistrations,
    property: CSHasChangeValue<V>,
    onChange: ArgFunc<V>
): CSRegistration = onChange(
    parent + property.action { onChange(property.value) },
    onChange = { onChange(property.value) }
)

fun <T : Preset> T.onChangePause(
    vararg registrations: CSRegistration,
): CSRegistration = CSRegistration(
    onBeforeChange { registrations.pause() },
    onChange { registrations.resume() }
)

fun <T : Preset> T.onChange(
    vararg registrations: CSRegistration,
    before: Func,
    onChange: Func
): CSRegistration = CSRegistration(
    onBeforeChange {
        registrations.pause()
        before()
    }, onChange {
        onChange()
        registrations.resume()
    }
)