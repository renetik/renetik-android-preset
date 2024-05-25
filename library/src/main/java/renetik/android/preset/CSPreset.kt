package renetik.android.preset

import renetik.android.core.kotlin.unexpected
import renetik.android.core.lang.value.isTrue
import renetik.android.core.lang.variable.setFalse
import renetik.android.core.lang.variable.setTrue
import renetik.android.event.CSEvent.Companion.event
import renetik.android.event.common.CSHasRegistrationsHasDestruct
import renetik.android.event.common.CSModel
import renetik.android.event.listenOnce
import renetik.android.event.property.CSProperty.Companion.property
import renetik.android.event.registration.CSHasChange
import renetik.android.event.registration.plus
import renetik.android.preset.property.CSPresetKeyData
import renetik.android.store.CSStore
import renetik.android.store.extensions.property
import renetik.android.store.type.CSJsonObjectStore

class CSPreset<PresetListItem : CSPresetItem, PresetList : CSPresetDataList<PresetListItem>>(
    parent: CSHasRegistrationsHasDestruct, val parentStore: CSStore,
    key: String, val list: PresetList,
    notFoundItem: () -> PresetListItem, defaultItemId: String? = null,
) : CSModel(parent), CSHasChange<Unit> {

    companion object {
        fun <PresetItem : CSPresetItem, PresetList : CSPresetDataList<PresetItem>> CSPreset(
            parent: CSHasRegistrationsHasDestruct, parentPreset: CSPreset<*, *>,
            key: String, list: PresetList,
            notFoundItem: () -> PresetItem, defaultItemId: String? = null,
        ): CSPreset<PresetItem, PresetList> = CSPreset(
            parent, parentPreset.store, key, list, notFoundItem, defaultItemId
        ).also(parentPreset::add)

        fun <Parent, PresetItem : CSPresetItem, Presets : CSPresetDataList<PresetItem>> CSPreset(
            parent: Parent,
            key: String,
            list: Presets,
            notFoundItem: () -> PresetItem,
            defaultItemId: String? = null
        ): CSPreset<PresetItem, Presets> where Parent : CSHasPreset, Parent : CSHasRegistrationsHasDestruct =
            CSPreset(
                parent, parent.preset, key = "${parent.presetId} $key",
                list, notFoundItem, defaultItemId
            )
    }

    val id = "$key preset"
    val isFollowStore = property(true)
    val isPresetReload = property(false)

    val eventLoad = event<PresetListItem>()
    val eventSave = event<PresetListItem>()
    val eventChange = event<PresetListItem>()
    val eventDelete = event<PresetListItem>()

    val store = CSPresetStore(this)
    val listItem = CSPresetListItem(this, notFoundItem, defaultItemId)

    val title = store.property(this, "preset title", default = "")
    val properties = mutableListOf<CSPresetKeyData>()
    val presets = mutableListOf<CSPreset<*, *>>()

    val isModified: Boolean get() = isModifiedIn(listItem.value.store)

    fun clear() {
        store.clear()
        listItem.clear()
    }

    private fun isModifiedIn(store: CSStore): Boolean =
        properties.any { it.isModifiedIn(store) } ||
                presets.any { preset ->
                    if (store.has(preset.store.key)) {
                        val presetStoreInItem = CSJsonObjectStore().apply {
                            load(store.getMap(preset.store.key)!!)
                        }
                        preset.isModifiedIn(presetStoreInItem)
                    } else preset.isModifiedIn(preset.listItem.value.store)
                }

    fun <T : CSPresetKeyData> add(property: T): T {
        if (properties.contains(property)) unexpected()
        properties += property
        property.eventDestruct.listenOnce { properties -= property }
        return property
    }

    private fun add(preset: CSPreset<*, *>) {
        if (presets.contains(preset)) unexpected()
        preset + isPresetReload.onChange { preset.isPresetReload.value = it }
        add(preset.listItem)
        presets += preset
        preset.eventDestruct.listenOnce { presets -= preset }
    }

    fun reload() = reload(listItem.value)

    fun reload(item: PresetListItem) {
        val isAlreadyReloading = isPresetReload.isTrue
        if (!isAlreadyReloading) isPresetReload.setTrue()
        eventLoad.fire(item)
        reload(item.store.data)
        eventChange.fire(item)
        if (!isAlreadyReloading) isPresetReload.setFalse()
    }

    internal fun reload(data: Map<String, Any?>) {
        store.reload(data)
        properties.toList().forEach(CSPresetKeyData::onStoreLoaded)
        presets.toList().forEach { it.store.onStoreLoaded() }
    }

    fun saveAsNew(item: PresetListItem) {
        eventSave.fire(item) // Order important
        listItem.value(item)
    }

    fun delete(item: PresetListItem) {
        list.remove(item)
        eventDelete.fire(item)
    }

    override fun toString() = "$id ${super.toString()}"

    fun onBeforeChange(function: () -> Unit) = eventLoad.listen { function() }

    override fun onChange(function: (Unit) -> Unit) = eventChange.listen { function(Unit) }

    fun saveAsCurrent() = eventSave.fire(listItem.value)
}