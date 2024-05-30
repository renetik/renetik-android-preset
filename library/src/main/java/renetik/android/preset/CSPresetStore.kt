package renetik.android.preset

import renetik.android.core.lang.value.isFalse
import renetik.android.event.common.CSLaterOnceFunc.Companion.laterOnce
import renetik.android.event.common.destruct
import renetik.android.preset.property.CSPresetKeyData
import renetik.android.store.CSStore
import renetik.android.store.type.CSJsonObjectStore

class CSPresetStore(
    override val preset: CSPreset<*, *>,
) : CSJsonObjectStore(), CSPresetKeyData {
    val parentStore: CSStore = preset.parentStore
    override val key = "${preset.id} store"
    override fun saveTo(store: CSStore) = store.set(key, data)
    var isSavedToParentPresetAsDefaultWithoutChildStores = false

    override val isDestructed: Boolean get() = preset.isDestructed
    override val eventDestruct get() = preset.eventDestruct
    override fun onDestruct() = preset.destruct()
    private val saveToParentStore = preset.laterOnce { saveTo(parentStore) }

    init {
        parentStore.getMap(key)?.let(::load)
    }

    override fun onStoreLoaded() {
        if (preset.isFollowStore.isFalse) saveToParentStore()
        else {
            val data = parentStore.getMap(key)
            if (this.data == data) return
            if (data.isNullOrEmpty()) preset.reload()
            else preset.reload(data)
        }
    }

    override fun onChanged() {
        super.onChanged()
        saveToParentStore()
    }

    override fun equals(other: Any?) = (other as? CSPresetStore)
        ?.let { it.key == key && super.equals(other) } ?: super.equals(other)

    override fun hashCode() = 31 * key.hashCode() + super.hashCode()

    override fun toString() = "key:$key ${super.toString()}"
}