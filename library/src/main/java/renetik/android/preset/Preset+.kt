package renetik.android.preset

import renetik.android.core.kotlin.changeIf
import renetik.android.core.lang.ArgFunc
import renetik.android.core.lang.Func
import renetik.android.core.lang.to
import renetik.android.core.lang.value.isTrue
import renetik.android.event.common.CSHasRegistrationsHasDestruct
import renetik.android.event.common.CSLaterOnceFunc.Companion.laterOnceFunc
import renetik.android.event.common.onDestructed
import renetik.android.event.property.CSProperty
import renetik.android.event.property.CSProperty.Companion.property
import renetik.android.event.property.connect
import renetik.android.event.registration.CSHasChange
import renetik.android.event.registration.CSHasChange.Companion.action
import renetik.android.event.registration.CSHasChangeValue
import renetik.android.event.registration.CSHasChangeValue.Companion.hasChangeValue
import renetik.android.event.registration.CSHasRegistrations
import renetik.android.event.registration.CSRegistration
import renetik.android.event.registration.CSRegistrationsMap
import renetik.android.event.registration.action
import renetik.android.event.registration.onChange
import renetik.android.event.registration.pause
import renetik.android.event.registration.plus
import renetik.android.event.registration.resume
import kotlin.time.Duration.Companion.milliseconds

fun <PresetListItem : CSPresetItem,
        PresetList : CSPresetItemList<PresetListItem>,
        Preset : CSPreset<PresetListItem, PresetList>>
        Preset.init(): Preset = apply {
    if (store.data.isEmpty()) reload(listItem.value)
}

fun <T : Preset> T.followStoreIf(property: CSProperty<Boolean>) =
    isFollowStore.connect(property)

fun <T : Preset> T.onChange(
    before: Func,
    onChange: Func
): CSRegistration = CSRegistration(
    onBeforeChange { before() },
    onChange { onChange() }
)

fun <T : Preset> T.onChangePause(
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
): CSRegistration = onChangePause(
    parent + hasChange.onChange { onChange() },
    onChange = { onChange() }
)

fun <T : Preset> T.action(
    parent: CSHasRegistrations,
    hasChange: CSHasChange<*>,
    onChange: Func
): CSRegistration = onChangePause(
    parent + hasChange.action { onChange() },
    onChange = { onChange() }
)

fun <T> CSHasChangeValue<T>.onChangePausedBy(
    preset: CSPreset<*, *>, function: (T) -> Unit
): CSRegistration {
    val propertyActionRegistration = onChange(function)
    return CSRegistration(propertyActionRegistration, preset.onChangePause(
        propertyActionRegistration, onChange = { function(value) }
    ))
}

fun <T> CSHasChangeValue<T>.actionPausedBy(
    preset: CSPreset<*, *>, function: (T) -> Unit
): CSRegistration {
    val propertyActionRegistration = action(function)
    return CSRegistration(propertyActionRegistration, preset.onChangePause(
        propertyActionRegistration, onChange = { function(value) }
    ))
}

fun CSHasChange<*>.onChangePausedBy(
    preset: CSPreset<*, *>, function: () -> Unit
): CSRegistration {
    val propertyActionRegistration = onChange(function)
    return CSRegistration(propertyActionRegistration, preset.onChangePause(
        propertyActionRegistration, onChange = { function() }
    ))
}

fun CSHasChange<*>.actionPausedBy(
    preset: CSPreset<*, *>, function: () -> Unit
): CSRegistration {
    val propertyActionRegistration = action(function)
    return CSRegistration(propertyActionRegistration, preset.onChangePause(
        propertyActionRegistration, onChange = { function() }
    ))
}

fun <T : Preset> T.onReloadPause(
    vararg registrations: CSRegistration,
): CSRegistration {
    if (isReload.isTrue) registrations.pause()
    return CSRegistration(
        onBeforeChange { registrations.pause() },
        onChange { registrations.resume() }
    )
}

fun <T : Preset> T.onReloadPause(
    vararg properties: CSProperty<*>,
): CSRegistration {
    if (isReload.isTrue) properties.forEach(CSProperty<*>::pause)
    return CSRegistration(
        onBeforeChange { properties.forEach(CSProperty<*>::pause) },
        onChange { properties.forEach(CSProperty<*>::resume) }
    )
}

fun <T : Preset> T.onChange(
    paused: CSRegistration,
    before: Func,
    onChange: Func
): CSRegistration = CSRegistration(
    onBeforeChange {
        paused.pause()
        before()
    }, onChange {
        onChange()
        paused.resume()
    }
)

fun <T : Preset> T.onChange(
    paused: List<CSRegistration>,
    before: Func,
    onChange: Func
): CSRegistration = CSRegistration(
    onBeforeChange {
        paused.pause()
        before()
    }, onChange {
        onChange()
        paused.resume()
    }
)

//TODO: write test to prove why it has to be like this to avoid leaks
// and why it has to be so complicated...
// also if this shell be used in the future as general solution it has to be
// reused for all clients of preset but calculated just when used
fun Preset.isModified(
    parent: CSHasRegistrationsHasDestruct
): CSHasChangeValue<Boolean> {
    val property = property(isModified)
    val registrations = CSRegistrationsMap(this)
    val update = registrations.laterOnceFunc(500.milliseconds) {
        property.value(isModified)
    }
    val storeOnChange = registrations + store.onChange { update() }
    registrations + onBeforeChange { storeOnChange.pause() }
    registrations + onChange { storeOnChange.resume(); update() }
    registrations + (listItem to listItem.value.store.eventLoaded to eventSave)
        .onChange { update() }
    this + registrations
    this + parent.onDestructed { registrations.cancel() }
    return property
}

fun Preset.title(
    parent: CSHasRegistrationsHasDestruct, withModified: Boolean = false
): CSHasChangeValue<String> = if (withModified) titleWithModified(parent) else title

fun Preset.titleWithModified(
    parent: CSHasRegistrationsHasDestruct
): CSHasChangeValue<String> = (title to isModified(parent)).hasChangeValue(
    this, from = { title, modified ->
        title.changeIf(String::isNotBlank) { "$it${if (modified) " *" else ""}" }
    })