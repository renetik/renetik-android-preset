package renetik.android.preset

import renetik.android.core.kotlin.toId
import renetik.android.core.lang.variable.isFalse
import renetik.android.core.logging.CSLog.logDebug
import renetik.android.core.logging.CSLogMessage.Companion.message
import renetik.android.event.CSEvent.Companion.event
import renetik.android.event.common.CSModel
import renetik.android.event.paused
import renetik.android.event.property.CSProperty
import renetik.android.event.registration.register
import renetik.android.json.obj.getValue
import renetik.android.preset.property.CSPresetKeyData
import renetik.android.store.CSStore

class CSPresetListItem<PresetItem : CSPresetItem,
    PresetList : CSPresetDataList<PresetItem>>(
    override val preset: CSPreset<PresetItem, PresetList>,
    private val store: CSStore,
    private val getDefault: (isSaved: Boolean) -> PresetItem
) : CSModel(preset), CSProperty<PresetItem>, CSPresetKeyData {

    override val key = "${preset.id} current"

    override fun saveTo(store: CSStore) = store.set(key, value.toId())
    private fun save() = saveTo(store)
    private val isSaved get() = store.has(key)

    private var loadedValue: PresetItem = loadValue()
    private fun loadValue() = store.getValue(key, preset.list.items)
        ?: getDefault(isSaved).also { save() }

    private val eventChange = event<PresetItem>()

    init {
        register(store.eventLoaded.listen { onParentStoreLoaded() })
    }

    private fun onParentStoreLoaded() {
        if (preset.isFollowStore.isFalse)
            parentStoreLoadedIsFollowStoreFalseSaveToParentStore()
        else {
            val newValue = loadValue()
            if (loadedValue == newValue) return
            loadedValue = newValue
            eventChange.fire(newValue)
        }
    }

    private fun parentStoreLoadedIsFollowStoreFalseSaveToParentStore() =
        store.eventChanged.paused { save() }

    override fun value(newValue: PresetItem, fire: Boolean) {
        if (loadedValue == newValue) return
        logDebug { message(newValue) }
        loadedValue = newValue
        if (fire) eventChange.fire(newValue)
        preset.reload(newValue)
        save()
    }

    override fun onChange(function: (PresetItem) -> Unit) = eventChange.listen(function)

    override var value: PresetItem
        get() = loadedValue
        set(value) = value(value)

    override fun toString() = "${super.toString()} key:$key value:$value"
    override fun fireChange() = eventChange.fire(loadedValue)
}