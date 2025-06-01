package renetik.android.preset

import renetik.android.core.kotlin.unexpected
import renetik.android.core.lang.value.isTrue
import renetik.android.core.lang.variable.setFalse
import renetik.android.core.lang.variable.setTrue
import renetik.android.event.CSEvent.Companion.event
import renetik.android.event.common.CSHasDestruct
import renetik.android.event.common.CSModel
import renetik.android.event.common.destruct
import renetik.android.event.listenOnce
import renetik.android.event.property.CSProperty.Companion.property
import renetik.android.event.property.computed
import renetik.android.event.registration.CSHasChange
import renetik.android.event.registration.plus
import renetik.android.preset.property.CSPresetKeyData
import renetik.android.store.CSStore
import renetik.android.store.type.CSJsonObjectStore.Companion.CSJsonObjectStore

class CSPreset<PresetListItem : CSPresetItem,
        PresetList : CSPresetItemList<PresetListItem>>(
    parent: CSHasDestruct, val parentStore: CSStore,
    key: String, val list: PresetList,
    notFoundItem: () -> PresetListItem, defaultItemId: String? = null,
) : CSModel(parent), CSHasChange<Unit> {

    companion object {
        fun <PresetItem : CSPresetItem,
                PresetList : CSPresetItemList<PresetItem>> CSPreset(
            parent: CSHasDestruct, parentPreset: CSPreset<*, *>,
            key: String, list: PresetList,
            notFoundItem: () -> PresetItem, defaultItemId: String? = null,
        ): CSPreset<PresetItem, PresetList> = CSPreset(
            parent, parentPreset.store, key, list, notFoundItem, defaultItemId
        ).also(parentPreset::add)
    }

    val id = "$key preset"
    val isFollowStore = property(true)
    val isReload = property(false)

    val eventLoad = event<PresetListItem>()
    val eventSave = event<PresetListItem>()
    val eventChange = event<PresetListItem>()
    val eventDelete = event<PresetListItem>()

    val store = CSPresetStore(this)
    val listItem = CSPresetListItem(this, notFoundItem, defaultItemId)

    val title = listItem.computed(child = { it.title })

    val properties = mutableListOf<CSPresetKeyData>()
    val presets = mutableListOf<CSPreset<*, *>>()

    fun clear() {
        store.clear()
        store.clearKeyData()
        listItem.clearKeyData()
        properties.toList().forEach { it.clear() }
    }

    fun destructClear() {
        destruct()
        listItem.clearKeyData()
        store.clearKeyData()
    }

    val isModified: Boolean get() = isModifiedIn(listItem.value.store)

    private fun isModifiedIn(store: CSStore): Boolean =
        properties.any { it.isTrackedModifiedIn(store) } || presets.any {
            store.getMap(it.store.key)?.let { data ->
                it.isModifiedIn(CSJsonObjectStore(data))
            } ?: it.isModifiedIn(it.listItem.value.store)
        }

    fun <T : CSPresetKeyData> add(property: T): T {
        if (property in properties) unexpected()
        properties += property
        property.eventDestruct.listenOnce { properties -= property }
        return property
    }

    private fun add(preset: CSPreset<*, *>) {
        if (preset in presets) unexpected()
        preset + isReload.onChange { preset.isReload.value = it }
        add(preset.listItem)
//        add(preset.store)
        presets += preset
        preset.eventDestruct.listenOnce { presets -= preset }
    }

    fun reload() = reload(listItem.value)

    fun reload(item: PresetListItem) {
        val isAlreadyReloading = isReload.isTrue
        if (!isAlreadyReloading) isReload.setTrue()
        isReload.setTrue()
        eventLoad.fire(item)
        reload(item.store.data)
        eventChange.fire(item)
        if (!isAlreadyReloading) isReload.setFalse()
    }

    fun reload(data: Map<String, Any?>) {
//        val isAlreadyReloading = isReload.isTrue
//        if (!isAlreadyReloading) isReload.setTrue()
        store.reload(data)
        store.saveToParentStore()
        properties.toList().forEach { if (!it.isDestructed) it.onStoreLoaded() }
        presets.toList().forEach {
            if (!it.isDestructed) it.store.onStoreLoaded()
        }
//        if (!isAlreadyReloading) isReload.setFalse()
    }

    fun save(item: PresetListItem) = eventSave.fire(item)

    fun delete(item: PresetListItem) {
        list.remove(item)
        eventDelete.fire(item)
    }

    override fun toString() = "$id ${super.toString()}"

    fun onBeforeChange(function: () -> Unit) = eventLoad.listen { function() }

    override fun onChange(function: (Unit) -> Unit) =
        eventChange.listen { function(Unit) }

    fun saveAsCurrent() = eventSave.fire(listItem.value)

    @Suppress("UNUSED_ANONYMOUS_PARAMETER")
    var onSaveToParentPresetItemStore: (Boolean, CSStore) -> Unit =
        { isDefault, itemStore -> this.store.saveTo(itemStore) }
}