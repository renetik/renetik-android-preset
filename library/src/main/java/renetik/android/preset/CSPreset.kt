package renetik.android.preset

import renetik.android.core.kotlin.unexpected
import renetik.android.core.lang.CSHasId
import renetik.android.core.lang.void
import renetik.android.event.CSEvent.Companion.event
import renetik.android.event.common.CSHasRegistrationsHasDestruct
import renetik.android.event.common.CSModel
import renetik.android.event.listenOnce
import renetik.android.event.property.CSProperty.Companion.property
import renetik.android.event.registration.CSHasChange
import renetik.android.preset.property.CSPresetKeyData
import renetik.android.store.CSStore
import renetik.android.store.extensions.property
import renetik.android.store.extensions.reload
import kotlin.reflect.KClass

class CSPreset<
    PresetListItem : CSPresetItem,
    PresetList : CSPresetDataList<PresetListItem>,
    >(
    parent: CSHasRegistrationsHasDestruct, parentStore: CSStore,
    key: String, val list: PresetList,
    notFoundItem: KClass<out PresetListItem>,
    defaultItemId: String? = null,
) : CSModel(parent), CSHasId, CSHasChange<Unit> {

    constructor(
        parent: CSHasRegistrationsHasDestruct, preset: CSPreset<*, *>,
        key: String, list: PresetList,
        notFoundItem: KClass<out PresetListItem>, defaultItemId: String? = null,
    ) : this(parent, preset.store, key, list, notFoundItem, defaultItemId) {
        preset.add(listItem)
        preset.add(store)
    }

    constructor(
        parent: CSHasPreset, key: String, list: PresetList,
        notFoundItem: KClass<out PresetListItem>,
        defaultItemId: String? = null,
    ) : this(
        parent, parent.preset, key = "${parent.presetId} $key",
        list, notFoundItem, defaultItemId
    )

    override val id = "$key preset"
    val isFollowStore = property(true)
    val eventReload = event<PresetListItem>()
    val eventAfterReload = event<PresetListItem>()

    //listItem first, store second so they listen load in right order
    val listItem = CSPresetListItem(this, parentStore, notFoundItem, defaultItemId)
    val store = CSPresetStore(this, parentStore)

    val title = store.property(this, "preset title", default = "")
//    val item: PresetListItem = notFoundItem.createInstance(store)!! //TODO

    private val dataList = mutableListOf<CSPresetKeyData>()

    init {
        if (store.data.isEmpty()) reload(listItem.value)
    }

    fun reload() = reload(listItem.value)

    fun reload(item: PresetListItem) {
        eventReload.fire(item)
        store.reload(item.store)
        eventAfterReload.fire(item)
    }

    fun <T : CSPresetKeyData> add(property: T): T {
        if (dataList.contains(property)) unexpected()
        dataList.add(property)
        property.eventDestruct.listenOnce { dataList.remove(property) }
        return property
    }

    fun saveAsNew(item: PresetListItem) {
        item.save(dataList)
        this.listItem.value(item)
    }

    fun saveAsCurrent() = listItem.value.save(dataList)

    override fun toString() = "$id ${super.toString()}"

    fun onBeforeChange(function: () -> Unit) = eventReload.listen { function() }

    override fun onChange(function: (Unit) -> void) = eventAfterReload.listen { function(Unit) }
}