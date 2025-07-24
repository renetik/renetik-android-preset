package renetik.android.preset

import renetik.android.core.kotlin.toId
import renetik.android.core.lang.value.isFalse
import renetik.android.event.common.parent
import renetik.android.event.paused
import renetik.android.event.property.CSProperty
import renetik.android.event.property.CSPropertyWrapper
import renetik.android.event.property.computed
import renetik.android.event.registration.invoke
import renetik.android.event.registration.plus
import renetik.android.preset.property.CSPresetKeyData
import renetik.android.store.CSStore
import renetik.android.store.extensions.isNotEmpty
import renetik.android.store.extensions.property
import renetik.android.store.property.saveTo

class CSPresetListItem<
        PresetItem : CSPresetItem,
        PresetList : CSPresetItemList<PresetItem>,
        >(
    override val preset: CSPreset<PresetItem, PresetList>,
    private val notFoundPresetItem: (CSStore) -> PresetItem,
    private val defaultItemId: String? = null,
) : CSPropertyWrapper<PresetItem>(preset), CSPresetKeyData {
    private val parentStore: CSStore = preset.parentStore
    override val key = "${preset.id} current"
    private val notFoundItem by lazy { notFoundPresetItem(preset.store) }

    override val property = parentStore.property(
        key, preset.list::items, getDefault = {
            if (parentStore.has(key) && preset.store.isNotEmpty) notFoundItem
            else getDefaultItem()
        }
    )

    init {
        property.parent(this)
//            .save() // Change: Not saving preset item on load and store load
        this + property.store.eventLoaded {
            if (preset.isFollowStore.isFalse)
                it.eventChanged.paused { property.saveTo(it) }
            else property.update()
        }
    }

    override fun saveTo(store: CSStore) = store.set(key, value.toId())
    override fun clearKeyData() = property.clear()
    override fun clear() = property.clear()
    override fun onStoreLoaded() = Unit
//        property.save() // Change: Not saving preset item on load and store load

    val currentId: CSProperty<String> = computed(from = { it.id }, to = { presetId ->
        preset.list.items.find { it.id == presetId } ?: notFoundPresetItem(preset.store)
    })

    override fun isTrackedModifiedIn(store: CSStore) =
        if (store.has(key)) currentId.value != store.get(key)
        else currentId.value != getDefaultItem().id

    private fun getDefaultItem(): PresetItem =
        preset.list.defaultItems.find { it.id == defaultItemId }
            ?: preset.list.defaultItems[0]

    override fun value(newValue: PresetItem, fire: Boolean) {
        val isPresetReload = value != newValue
        super.value(newValue, fire)
        if (isPresetReload) preset.reload(newValue)
    }

    override fun toString() = "key:$key this:${super.toString()}"
}