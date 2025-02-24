package renetik.android.preset

import renetik.android.core.lang.value.isFalse
import renetik.android.event.common.destruct
import renetik.android.event.common.onDestructed
import renetik.android.preset.property.CSPresetKeyData
import renetik.android.store.CSStore
import renetik.android.store.type.CSJsonObjectStore

class CSPresetStore(
    override val preset: CSPreset<*, *>,
) : CSJsonObjectStore(), CSPresetKeyData {
    private val parentStore: CSStore = preset.parentStore
    override val key = "${preset.id} store"
    override fun saveTo(store: CSStore) = store.set(key, data)

    override val isDestructed: Boolean get() = preset.isDestructed
    override val eventDestruct get() = preset.eventDestruct
    override fun onDestruct() = preset.destruct()

//    private val saveLater: CSFunc = preset.laterOnceFunc(::saveNow)

    private var pendingSave: Boolean = false

    private fun saveToParentStore() {
//        pendingSave = true; saveLater() //Temporary to find cause of issues ?
        saveNow()
    }

    private fun saveNow() {
        pendingSave = false; saveTo(parentStore)
    }

    override fun clearKeyData() = parentStore.clear(key)

    init {
        parentStore.getMap(key)?.let(::load)
        preset.onDestructed { if (pendingSave) saveNow() }
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

    override fun equals(other: Any?) =
        (other as? CSPresetStore)?.let { it.key == key && super.equals(other) }
            ?: super.equals(other)

    override fun hashCode() = 31 * key.hashCode() + super.hashCode()

    override fun toString() = "key:$key ${super.toString()}"
}