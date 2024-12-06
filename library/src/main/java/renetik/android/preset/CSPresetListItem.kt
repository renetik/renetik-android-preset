package renetik.android.preset

import renetik.android.core.kotlin.toId
import renetik.android.event.property.CSProperty
import renetik.android.event.property.CSPropertyWrapper
import renetik.android.event.property.delegate
import renetik.android.preset.property.CSPresetKeyData
import renetik.android.store.CSStore
import renetik.android.store.extensions.property
import renetik.android.store.property.save

class CSPresetListItem<
        PresetItem : CSPresetItem,
        PresetList : CSPresetItemList<PresetItem>,
        >(
    override val preset: CSPreset<PresetItem, PresetList>,
    private val notFoundPresetItem: () -> PresetItem,
    private val defaultItemId: String? = null,
) : CSPropertyWrapper<PresetItem>(preset), CSPresetKeyData {
    private val parentStore: CSStore = preset.parentStore
    override val key = "${preset.id} current"

    override val property = parentStore.property(
        this, key, preset.list::items, getDefault = ::getDefaultItem
    ).also { it.save() }

    override fun saveTo(store: CSStore) = store.set(key, property.value.toId())

    val currentId: CSProperty<String> = delegate(from = { it.id }, to = { presetId ->
        preset.list.items.find { it.id == presetId } ?: notFoundPresetItem()
    })

    override fun onStoreLoaded() = property.save()

    override fun clearKeyData() = parentStore.clear(key)

    override fun clear() = property.clear()

    override fun isTrackedModifiedIn(store: CSStore) =
        if (store.has(key)) currentId.value != store.get(key)
        else currentId.value != getDefaultItem().id

    private fun getDefaultItem(): PresetItem =
        preset.list.defaultItems.find { it.id == defaultItemId }
            ?: preset.list.defaultItems[0]

    override fun value(newValue: PresetItem, fire: Boolean) {
        super.value(newValue, fire)
        preset.reload(newValue)
    }

    override fun toString() = "key:$key ${super.toString()}"
}