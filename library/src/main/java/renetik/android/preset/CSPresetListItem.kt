package renetik.android.preset

import renetik.android.core.kotlin.toId
import renetik.android.core.lang.lazy.CSLazyVar.Companion.lazyVar
import renetik.android.core.lang.value.isFalse
import renetik.android.event.CSEvent.Companion.event
import renetik.android.event.common.CSModel
import renetik.android.event.paused
import renetik.android.event.property.CSProperty
import renetik.android.event.registration.plus
import renetik.android.preset.property.CSPresetKeyData
import renetik.android.store.CSStore
import renetik.android.store.extensions.property
import renetik.android.store.property.CSStoreProperty
import renetik.android.store.property.isSaved

class CSPresetListItem<
        PresetItem : CSPresetItem,
        PresetList : CSPresetDataList<PresetItem>,
        >(
    override val preset: CSPreset<PresetItem, PresetList>,
    parentStore: CSStore,
    private val notFoundPresetItem: () -> PresetItem,
    private val defaultItemId: String? = null,
) : CSModel(preset), CSProperty<PresetItem>, CSPresetKeyData {

    override val key = "${preset.id} current"
    override fun saveTo(store: CSStore) = store.set(key, _value.toId())
    val currentId: CSStoreProperty<String> = parentStore.property(this, key, defaultItemId ?: "")

    private fun save(value: PresetItem) = currentId.value(value.toId())
    private var _value: PresetItem by lazyVar(::loadValue)
    private val eventChange = event<PresetItem>()

    init {
        this + parentStore.eventLoaded.listen { onParentStoreLoaded(it) }
    }

    private fun onParentStoreLoaded(store: CSStore) {
        if (preset.isFollowStore.isFalse)
            store.eventChanged.paused { save(_value) }
        else {
            val newValue = loadValue()
            if (_value == newValue) return
            _value = newValue
            eventChange.fire(newValue)
        }
    }

    private fun loadValue(): PresetItem =
        (preset.list.items.find { it.toId() == currentId.value }
            ?: onItemNotFound()).also(::save)

    private fun onItemNotFound(): PresetItem =
        if (currentId.isSaved) notFoundPresetItem()
        else preset.list.defaultItems.find { it.id == defaultItemId }
            ?: preset.list.defaultItems[0]

    override fun value(newValue: PresetItem, fire: Boolean) {
        if (_value == newValue) return
        _value = newValue
        if (fire) eventChange.fire(newValue)
        save(_value)
        preset.reload(newValue)
    }

    override fun onChange(function: (PresetItem) -> Unit) = eventChange.listen(function)

    override var value: PresetItem
        get() = _value
        set(value) = value(value)

    override fun toString() = "${super.toString()} key:$key"
    override fun fireChange() = eventChange.fire(_value)
}