package renetik.android.preset

import renetik.android.core.kotlin.toId
import renetik.android.core.lang.variable.isFalse
import renetik.android.event.CSEvent.Companion.event
import renetik.android.event.common.CSModel
import renetik.android.event.property.CSProperty
import renetik.android.event.registration.register
import renetik.android.event.registration.pause
import renetik.android.json.obj.getValue
import renetik.android.preset.property.CSPresetKeyData
import renetik.android.store.CSStore

class CSPresetStoreItemProperty<PresetItem : CSPresetItem,
        PresetList : CSPresetItemList<PresetItem>>(
    override val preset: CSPreset<PresetItem, PresetList>,
    val parentStore: CSStore,
    val getDefault: () -> PresetItem
) : CSModel(preset), CSProperty<PresetItem>, CSPresetKeyData {

    override val key = "${preset.id} current"
    override fun saveTo(store: CSStore) = store.set(key, value.toId())
    private var _value: PresetItem = loadValue()
    private fun loadValue() = parentStore.getValue(key, preset.list.items) ?: getDefault()

    private val eventChange = event<PresetItem>()

    private val parentStoreChanged =
        register(parentStore.eventChanged.listen { onParentStoreChange() })

    private fun onParentStoreChange() {
        if (preset.isFollowStore.isFalse)
            parentStoreChangedIsFollowStoreFalseSaveToParentStore()
        else {
            val newValue = loadValue()
            if (_value == newValue) return
            _value = newValue
            parentStoreChanged.pause().use { eventChange.fire(newValue) }
        }
    }

    private fun parentStoreChangedIsFollowStoreFalseSaveToParentStore() =
        parentStore.eventChanged.pause().use { saveTo(parentStore) }

    override fun value(newValue: PresetItem, fire: Boolean) {
        if (_value == newValue) return
        _value = newValue
        parentStoreChanged.pause().use {
            if (fire) eventChange.fire(newValue)
            preset.reload(newValue)
            saveTo(parentStore)
        }
    }

    override fun onChange(function: (PresetItem) -> Unit) = eventChange.listen(function)

    override var value: PresetItem
        get() = _value
        set(value) = value(value)

    override fun toString() = "${super.toString()} key:$key value:$value"
    override fun fireChange() = eventChange.fire(_value)
}