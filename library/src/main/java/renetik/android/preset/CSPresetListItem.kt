package renetik.android.preset

import renetik.android.core.kotlin.reflect.createInstance
import renetik.android.core.kotlin.toId
import renetik.android.core.lang.lazy.CSLazyVar.Companion.lazyVar
import renetik.android.core.lang.variable.isFalse
import renetik.android.event.CSEvent.Companion.event
import renetik.android.event.common.CSModel
import renetik.android.event.paused
import renetik.android.event.property.CSProperty
import renetik.android.event.registration.register
import renetik.android.preset.property.CSPresetKeyData
import renetik.android.store.CSStore
import renetik.android.store.extensions.property
import renetik.android.store.property.CSStoreProperty
import renetik.android.store.property.isSaved
import kotlin.reflect.KClass

class CSPresetListItem<
    PresetItem : CSPresetItem,
    PresetList : CSPresetDataList<PresetItem>,
    >(
    override val preset: CSPreset<PresetItem, PresetList>,
    private val store: CSStore,
    private val notFoundPresetItem: KClass<out PresetItem>,
    private val defaultItemId: String? = null,
) : CSModel(preset), CSProperty<PresetItem>, CSPresetKeyData {

    override val key = "${preset.id} current"
    override fun saveTo(store: CSStore) = store.set(key, _value.toId())
    val currentId: CSStoreProperty<String> = store.property(this, key, defaultItemId ?: "")

    private fun save(value: PresetItem) = currentId.value(value.toId())
    private var _value: PresetItem by lazyVar(::loadValue)
    private val eventChange = event<PresetItem>()

    init {
        register(store.eventLoaded.listen { onParentStoreLoaded() })
    }

    private fun onParentStoreLoaded() {
        if (preset.isFollowStore.isFalse)
            parentStoreLoadedIsFollowStoreFalseSaveToParentStore()
        else {
            val newValue = loadValue()
            if (_value == newValue) return
            _value = newValue
            eventChange.fire(newValue)
        }
    }

    private fun loadValue(): PresetItem =
        preset.list.items.find { it.toId() == currentId.value } ?: let {
            if (currentId.isSaved) notFoundPresetItem.createInstance(preset.store)!!
            else preset.list.defaultItems.find { it.id == defaultItemId }
                ?: preset.list.defaultItems[0]
        }.also { save(it) }

    private fun parentStoreLoadedIsFollowStoreFalseSaveToParentStore() =
        store.eventChanged.paused { save(_value) }

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

    override fun toString() = "${super.toString()} key:$key value:$_value"
    override fun fireChange() = eventChange.fire(_value)
}