package renetik.android.preset

import renetik.android.core.lang.ArgFunc
import renetik.android.core.lang.Func
import renetik.android.core.lang.to
import renetik.android.event.common.CSHasRegistrationsHasDestruct
import renetik.android.event.common.CSLaterOnceFunc.Companion.laterOnce
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
import renetik.android.event.registration.onChange
import renetik.android.event.registration.pause
import renetik.android.event.registration.plus
import renetik.android.event.registration.resume

fun <PresetListItem : CSPresetItem,
        PresetList : CSPresetDataList<PresetListItem>,
        Preset : CSPreset<PresetListItem, PresetList>> Preset.init(): Preset =
    apply {
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
    property: CSHasChangeValue<V>,
    onChange: ArgFunc<V>
): CSRegistration {
    val propertyActionRegistration = property.action { onChange(property.value) }
    return CSRegistration(propertyActionRegistration, onChange(
        propertyActionRegistration,
        onChange = { onChange(property.value) }
    ))
}

fun <T : Preset> T.onChangePause(
    vararg registrations: CSRegistration,
): CSRegistration = CSRegistration(
    onBeforeChange { registrations.pause() },
    onChange { registrations.resume() }
)

//fun <T : Preset> T.onChange(
//    vararg registrations: CSRegistration,
//    before: Func,
//    onChange: Func
//): CSRegistration = CSRegistration(
//    onBeforeChange {
//        registrations.pause()
//        before()
//    }, onChange {
//        onChange()
//        registrations.resume()
//    }
//)

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
    val update = registrations.laterOnce(after = 500) { property.value(isModified) }
    val storeOnChange = registrations + store.onChange { update() }
    registrations + onBeforeChange { storeOnChange.pause() }
    registrations + onChange { storeOnChange.resume(); update() }
    registrations + (listItem to listItem.value.store.eventLoaded to eventSave)
        .onChange { update() }
    this + registrations
    this + parent.onDestructed { registrations.cancel() }
//    this + (parent + registrations) // TODO: And why this is not ok..
    return property
}

fun Preset.title(
    parent: CSHasRegistrationsHasDestruct, withModified: Boolean = false
): CSHasChangeValue<String> = if (withModified) titleWithModified(parent) else title

fun Preset.titleWithModified(
    parent: CSHasRegistrationsHasDestruct
): CSHasChangeValue<String> = (title to isModified(parent)).hasChangeValue(
    this, from = { title, modified ->
        "$title${if (modified) " *" else ""}"
    })