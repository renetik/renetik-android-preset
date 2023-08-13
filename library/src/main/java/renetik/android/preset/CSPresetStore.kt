package renetik.android.preset

import renetik.android.core.lang.variable.isFalse
import renetik.android.event.common.destruct
import renetik.android.event.registration.register
import renetik.android.preset.property.CSPresetKeyData
import renetik.android.store.CSStore
import renetik.android.store.extensions.reload
import renetik.android.store.type.CSJsonObjectStore

class CSPresetStore(
    override val preset: CSPreset<*, *>,
    private val parentStore: CSStore
) : CSJsonObjectStore(), CSPresetKeyData {

    override val key = "${preset.id} store"
    override fun saveTo(store: CSStore) = store.set(key, data)
    var isSavedToPresetAsDefaultWithoutStores = false
    var isSavedToPresetAsDefault = false

    override val isDestructed: Boolean get() = preset.isDestructed
    override val eventDestruct get() = preset.eventDestruct
    override fun onDestruct() = preset.destruct()

    init {
        parentStore.getMap(key)?.let { data -> load(data) }
        preset.register(parentStore.eventLoaded.listen {
            if (preset.isFollowStore.isFalse) saveTo(parentStore)
            else onParentStoreLoaded(it.getMap(key) ?: emptyMap<String, Any>())
        })
    }

    private fun onParentStoreLoaded(data: Map<String, *>) {
        if (this.data == data) return
        if (data.isEmpty())
            reload(preset.listItem.value.store)
        else reload(data)
    }

    override fun onChanged() {
        super.onChanged()
        saveTo(parentStore)
    }

    override fun equals(other: Any?) = (other as? CSPresetStore)
        ?.let { it.key == key && super.equals(other) } ?: super.equals(other)

    override fun hashCode() = 31 * key.hashCode() + super.hashCode()

    //TODO: Way to clean preset data from residuals,
    // but we need to move title and description properties somehow to
    // CSPreset.dataList
    override fun reset() {
        data.clear()
        preset.dataList.forEach {
            if (it != this) it.reset()
            it.saveTo(this)
        }
    }
}