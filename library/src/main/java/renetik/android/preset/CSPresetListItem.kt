package renetik.android.preset

import renetik.android.core.kotlin.toId
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
    private val parentStore: CSStore,
    private val notFoundPresetItem: () -> PresetItem,
    private val defaultItemId: String? = null,
) : CSPropertyWrapper<PresetItem>(preset), CSPresetKeyData {
    override val key = "${preset.id} current"

    override val property = parentStore.property(
        this, key, preset.list::items, ::getDefaultItem
    ).also { it.save() }

    override fun saveTo(store: CSStore) = store.set(key, property.value.toId())

    val currentId = delegate(from = { it.id })

    override fun onStoreLoaded() = property.save()

    private fun getDefaultItem(): PresetItem =
        if (parentStore.has(key)) notFoundPresetItem()
        else preset.list.defaultItems.find { it.id == defaultItemId }
            ?: preset.list.defaultItems[0]

    override fun value(newValue: PresetItem, fire: Boolean) {
        super.value(newValue, fire)
        preset.reload(newValue)
    }

    override fun toString() = "$key ${super.toString()}"
}