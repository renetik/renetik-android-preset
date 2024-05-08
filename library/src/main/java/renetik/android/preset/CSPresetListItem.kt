package renetik.android.preset

import renetik.android.core.kotlin.toId
import renetik.android.event.common.CSModel
import renetik.android.event.property.CSProperty
import renetik.android.event.registration.CSHasChangeValue
import renetik.android.event.registration.CSHasChangeValue.Companion.delegate
import renetik.android.event.registration.plus
import renetik.android.preset.property.CSPresetKeyData
import renetik.android.store.CSStore
import renetik.android.store.extensions.property
import renetik.android.store.property.CSStoreProperty
import renetik.android.store.property.save

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
    override fun saveTo(store: CSStore) = store.set(key, currentItem.value.toId())
//    val currentId: CSStoreProperty<String> = parentStore.property(this, key, defaultItemId ?: "")

//    private fun save(value: PresetItem) = currentItem.value(value)
//    private var _value: PresetItem by lazyVar(::loadValue)
//    private val eventChange = event<PresetItem>()

    val currentItem: CSStoreProperty<PresetItem> = parentStore.property(
        this, key, getValues = { preset.list.items }, getDefault = { getDefaultItem() })

    init {
        currentItem.save()
    }

    //
    val currentId: CSHasChangeValue<String> = currentItem.delegate(this, from = { it.id })

    init {
        this + parentStore.eventLoaded.listen {
//            onParentStoreChanged()
            currentItem.save()
        }
    }

//    private fun onParentStoreChanged() {
//        if (preset.isFollowStore.isFalse)
//            parentStore.eventChanged.paused { save(_value) }
//        else {
//            val newValue = loadValue()
//            if (_value == newValue) return
//            _value = newValue
//            eventChange.fire(newValue)
//        }
//    }

//    private fun loadValue(): PresetItem =
//        (preset.list.items.find { it.toId() == currentId.value }
//            ?: onItemNotFound()).also(::save)

    private fun getDefaultItem(): PresetItem =
//        if (currentItem.isSaved) notFoundPresetItem()
//        else
        preset.list.defaultItems.find { it.id == defaultItemId }
            ?: preset.list.defaultItems[0]

    override fun value(newValue: PresetItem, fire: Boolean) {
        currentItem.value(newValue, fire)
        preset.reload(newValue)
//        if (_value == newValue) return
//        _value = newValue
//        if (fire) eventChange.fire(newValue)
//        save(_value)
//        preset.reload(newValue)
    }

    init {
        this + currentItem.onChange {
            preset.reload(it)
        }
    }

    override fun onChange(function: (PresetItem) -> Unit) = currentItem.onChange(function)

    override var value: PresetItem
        get() = currentItem.value
        set(value) = value(value)

    override fun toString() = "${super.toString()} key:$key"
    override fun fireChange() = currentItem.fireChange()
}