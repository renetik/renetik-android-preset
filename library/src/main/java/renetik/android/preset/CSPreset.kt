package renetik.android.preset

import renetik.android.core.kotlin.unexpected
import renetik.android.core.lang.value.isTrue
import renetik.android.core.lang.variable.setFalse
import renetik.android.core.lang.variable.setTrue
import renetik.android.core.logging.CSLog
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
import renetik.android.store.extensions.reload

class CSPreset<PresetListItem : CSPresetItem, PresetList : CSPresetDataList<PresetListItem>>(
    parent: CSHasRegistrationsHasDestruct, parentStore: CSStore,
    key: String, val list: PresetList,
    notFoundItem: () -> PresetListItem, defaultItemId: String? = null,
) : CSModel(parent), CSHasChange<Unit> {

    companion object {
        fun <PresetItem : CSPresetItem, PresetList : CSPresetDataList<PresetItem>> CSPreset(
            parent: CSHasRegistrationsHasDestruct, parentPreset: CSPreset<*, *>,
            key: String, list: PresetList,
            notFoundItem: () -> PresetItem, defaultItemId: String? = null,
        ): CSPreset<PresetItem, PresetList> = CSPreset(
            parent, parentPreset.store, key,
            list, notFoundItem, defaultItemId
        ).apply {
            this + parentPreset.isPresetReload.onChange { isPresetReload.value = it }
            parentPreset.add(listItem)
            parentPreset.add(store)
        }

        fun <Parent, PresetItem : CSPresetItem, Presets : CSPresetDataList<PresetItem>> CSPreset(
            parent: Parent, key: String, list: Presets, notFoundItem: () -> PresetItem,
            defaultItemId: String? = null
        ): CSPreset<PresetItem, Presets>
                where Parent : CSHasPreset,
                      Parent : CSHasRegistrationsHasDestruct = CSPreset(
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

    //listItem first, store second so they listen load in right order
    val listItem = CSPresetListItem(this, parentStore, notFoundItem, defaultItemId)
    val store = CSPresetStore(this, parentStore)

    val title = store.property(this, "preset title", default = "")
    val data = mutableListOf<CSPresetKeyData>()

    fun reload() = reload(listItem.value)

    fun reload(item: PresetListItem) {
        val isAlreadyReloading = isPresetReload.isTrue
        if (!isAlreadyReloading) isPresetReload.setTrue()
        eventLoad.fire(item)
        store.reload(item.store)
        eventChange.fire(item)
        if (!isAlreadyReloading) isPresetReload.setFalse()
        CSLog.logInfo()
    }

    fun <T : CSPresetKeyData> add(property: T): T {
        if (data.contains(property)) unexpected()
        data.add(property)
        property.eventDestruct.listenOnce { data.remove(property) }
        return property
    }

    override fun toString() = "$id ${super.toString()}"

    fun onBeforeChange(function: () -> Unit) = eventLoad.listen { function() }

    override fun onChange(function: (Unit) -> Unit) = eventChange.listen { function(Unit) }

    fun saveAsCurrent() = eventSave.fire(listItem.value)

    fun saveAsNew(item: PresetListItem) {
        eventSave.fire(item) // Order important
        listItem.value(item)
    }

    fun delete(item: PresetListItem) {
        list.remove(item)
        eventDelete.fire(item)
    }
}