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
    internal val parentStore: CSStore = preset.parentStore
    override val key = "${preset.id} store"
    override fun saveTo(store: CSStore) = store.set(key, data)

    override val isDestructed: Boolean get() = preset.isDestructed
    override val eventDestruct get() = preset.eventDestruct
    override fun onDestruct() = preset.destruct()
    fun save() = saveTo(parentStore)
    override fun clearKeyData() = parentStore.clear(key)
//    val isNotStored get() = parentStore.getMap(key).isNullOrEmpty()

    init {
        parentStore.getMap(key)?.let(::load)
        preset.onDestructed { save() }
    }

    override fun onStoreLoaded() {
        if (preset.isFollowStore.isFalse) save()
        else {
            val data = parentStore.getMap(key)
            if (this.data == data) return
            if (data.isNullOrEmpty()) preset.reload()
            else preset.reload(data)
        }
    }

    override fun onChanged() {
        super.onChanged()
        if (preset.isReload.isFalse) save()
    }

    override fun toString() = "key:$key this:${super.toString()}"
    override fun hashCode() = 31 * key.hashCode() + super.hashCode()
    override fun equals(other: Any?) = (other as? CSPresetStore)?.let {
        it.key == key && super.equals(other)
    } ?: super.equals(other)
}