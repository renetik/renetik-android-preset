//package renetik.android.preset
//
//import renetik.android.core.lang.CSHasId
//import renetik.android.event.CSEvent
//import renetik.android.event.common.CSHasRegistrationsHasDestruct
//import renetik.android.event.common.CSModel
//import renetik.android.event.property.CSProperty
//import renetik.android.event.registration.CSHasChange
//import renetik.android.preset.property.CSPresetKeyData
//
//interface CSPresetInterface<
//    PresetListItem : CSPresetItem,
//    PresetList : CSPresetDataList<PresetListItem>,
//    > : CSHasRegistrationsHasDestruct, CSHasId, CSHasChange<Unit> {
//    val list: PresetList
//    val isFollowStore: CSProperty<Boolean>
//    val eventReload: CSEvent<PresetListItem>
//    val eventAfterReload: CSEvent<PresetListItem>
//    val listItem: CSPresetListItem<PresetListItem, PresetList>
//    val store: CSPresetStore
//    fun reload()
//    fun <T : CSPresetKeyData> add(property: T): T
//    fun saveAsNew(item: PresetListItem)
//    fun saveAsCurrent()
//    fun onBeforeChange(function: () -> Unit) = eventReload.listen { function() }
//    override fun onChange(function: (Unit) -> Unit) = eventAfterReload.listen { function(Unit) }
//}
//
//abstract class CSPresetWrapper<
//    PresetListItem : CSPresetItem,
//    PresetList : CSPresetDataList<PresetListItem>,
//    >(parent: CSHasRegistrationsHasDestruct) : CSModel(parent),
//    CSPresetInterface<PresetListItem, PresetList> {
//    abstract val preset: CSPresetInterface<PresetListItem, PresetList>
//    override val id get() = preset.id
//    override val list: PresetList get() = preset.list
//    override val isFollowStore get() = preset.isFollowStore
//    override val eventReload get() = preset.eventReload
//    override val eventAfterReload get() = preset.eventReload
//    override val listItem get() = preset.listItem
//    override val store get() = preset.store
//    override fun reload() = preset.reload()
//    override fun <T : CSPresetKeyData> add(property: T): T = preset.add(property)
//    override fun saveAsNew(item: PresetListItem) = preset.saveAsNew(item)
//    override fun saveAsCurrent() = preset.saveAsCurrent()
//}