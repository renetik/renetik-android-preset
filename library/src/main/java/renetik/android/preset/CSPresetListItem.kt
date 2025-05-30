package renetik.android.preset

import renetik.android.core.kotlin.toId
import renetik.android.event.property.CSProperty
import renetik.android.event.property.CSPropertyWrapper
import renetik.android.event.property.computed
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

    val currentId: CSProperty<String> = computed(from = { it.id }, to = { presetId ->
        preset.list.items.find { it.id == presetId } ?: notFoundPresetItem()
    })

    override fun onStoreLoaded() = property.save()

    override fun clearKeyData() = property.clear()

    override fun clear() = property.clear()

    override fun isTrackedModifiedIn(store: CSStore) =
        if (store.has(key)) currentId.value != store.get(key)
        else currentId.value != getDefaultItem().id

    private fun getDefaultItem(): PresetItem =
        preset.list.defaultItems.find { it.id == defaultItemId }
            ?: preset.list.defaultItems[0]

    //TODO: Sadly I totally not understand how this works now..
    private var isFirstSet = true
    override fun value(newValue: PresetItem, fire: Boolean) {
        val isPresetReload = isFirstSet || value != newValue
        super.value(newValue, fire)
        isFirstSet = false
        if (isPresetReload) preset.reload(newValue)
    }

    //TODO: Sadly I totally not understand how this works now..
    init {
        property.onValueChange.onChange {
            if (preset.store.isNotStoredInParent) {
                isFirstSet = false
                preset.reload(value)
            }
        }
    }

    override fun toString() = "key:$key this:${super.toString()}"
}