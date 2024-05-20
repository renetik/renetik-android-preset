package renetik.android.preset

import renetik.android.core.kotlin.toId
import renetik.android.event.property.CSProperty
import renetik.android.event.property.CSPropertyWrapper
import renetik.android.event.registration.CSHasChangeValue.Companion.delegate
import renetik.android.preset.property.CSPresetKeyData
import renetik.android.store.CSStore
import renetik.android.store.extensions.property
import renetik.android.store.property.save

class CSPresetListItem<
        PresetItem : CSPresetItem,
        PresetList : CSPresetDataList<PresetItem>,
        >(
    override val preset: CSPreset<PresetItem, PresetList>,
    parentStore: CSStore,
    private val notFoundPresetItem: () -> PresetItem,
    private val defaultItemId: String? = null,
) : CSPropertyWrapper<PresetItem>(preset), CSPresetKeyData {
    override val key = "${preset.id} current"
    override fun saveTo(store: CSStore) = store.set(key, currentItem.value.toId())

    val currentItem = parentStore.property(
        this, key, getValues = { preset.list.items },
        getDefault = { getDefaultItem() })

    val currentId = currentItem.delegate(this, from = { it.id })

    init {
        currentItem.save()
    }

    override fun onStoreLoaded() = currentItem.save()

    private fun getDefaultItem(): PresetItem =
//        if (currentItem.isSaved) notFoundPresetItem() else
        preset.list.defaultItems.find { it.id == defaultItemId }
            ?: preset.list.defaultItems[0]

    override fun value(newValue: PresetItem, fire: Boolean) {
        super.value(newValue, fire)
        preset.reload(newValue)
    }

    override val property: CSProperty<PresetItem> = currentItem

    override fun toString() = "$key ${super.toString()}"
}