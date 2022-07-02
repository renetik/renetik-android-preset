package renetik.android.preset

import renetik.android.core.kotlin.unexpected
import renetik.android.core.lang.CSHasId
import renetik.android.event.CSEvent.Companion.event
import renetik.android.event.fire
import renetik.android.event.listenOnce
import renetik.android.event.owner.CSEventOwnerHasDestroy
import renetik.android.event.owner.CSEventOwnerHasDestroyBase
import renetik.android.event.property.CSEventPropertyFunctions.property
import renetik.android.preset.property.CSPresetKeyData
import renetik.android.store.CSStore
import renetik.android.store.reload

class CSPreset<PresetItem : CSPresetItem, PresetList : CSPresetItemList<PresetItem>>(
    parent: CSEventOwnerHasDestroy,
    parentStore: CSStore,
    key: String,
    val list: PresetList,
    getDefault: (() -> PresetItem)? = null)
    : CSEventOwnerHasDestroyBase(parent), CSHasId {

    constructor (parent: CSEventOwnerHasDestroy, store: CSStore,
                 key: String, list: PresetList, defaultItemId: String)
            : this(parent, store, key, list, getDefault = {
        list.defaultItems.let { list -> list.find { it.id == defaultItemId } ?: list[0] }
    })

    constructor(parent: CSEventOwnerHasDestroy, preset: CSPreset<*, *>,
                key: String, list: PresetList,
                getDefault: (() -> PresetItem)? = null)
            : this(parent, preset.store, key, list, getDefault) {
        preset.add(item)
        preset.add(store)
    }

    constructor(parent: CSHasPreset, key: String, list: PresetList,
                getDefault: (() -> PresetItem)? = null) : this(parent,
        parent.preset, key = "${parent.presetId} $key", list, getDefault)

    override val id = "$key preset"
    val isFollowStore = property(true)
    val item = CSPresetStoreItemProperty(this, parentStore, getDefault ?: { list.items[0] })
    val store = CSPresetStore(this, parentStore)
    val eventReload = event()
    val eventAfterReload = event()
    private val dataList = mutableListOf<CSPresetKeyData>()

    init {
        if (store.data.isEmpty()) reload(item.value)
    }

    fun reload() = reload(item.value)

    fun reload(item: PresetItem) {
        eventReload.fire()
        store.reload(item.store)
        eventAfterReload.fire()
    }

    fun <T : CSPresetKeyData> add(property: T): T {
        if (dataList.contains(property)) unexpected()
        dataList.add(property)
        property.eventDestroy.listenOnce { dataList.remove(property) }
        return property
    }

    fun saveAsNew(item: PresetItem) {
        item.save(dataList)
        list.add(item)
        this.item.value(item)
    }

    fun saveAsCurrent() =
        item.value.save(dataList)

    override fun toString() = "$id ${super.toString()}"
}