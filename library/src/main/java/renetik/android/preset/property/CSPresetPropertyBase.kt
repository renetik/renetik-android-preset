package renetik.android.preset.property

import renetik.android.core.lang.lazy.CSLazyNullableVar.Companion.lazyNullableVar
import renetik.android.core.lang.value.isFalse
import renetik.android.event.common.CSHasDestruct
import renetik.android.event.paused
import renetik.android.event.property.CSProperty
import renetik.android.event.property.CSProperty.Companion.property
import renetik.android.event.property.CSPropertyBase
import renetik.android.preset.CSPreset
import renetik.android.store.CSStore

abstract class CSPresetPropertyBase<T>(
    parent: CSHasDestruct,
    final override val preset: CSPreset<*, *>,
    override val key: String,
    onChange: ((value: T) -> Unit)? = null
) : CSPropertyBase<T>(parent, onChange), CSPresetProperty<T> {

    protected abstract val default: T
    private var _value by lazyNullableVar { load() }
    protected abstract fun get(store: CSStore): T?
    protected abstract fun load(): T

    override var filter: ((T?) -> T?)? = null
    override fun getFiltered(store: CSStore): T? =
        get(store).let { filter?.invoke(it) ?: it }

    override fun saveTo(store: CSStore) = set(store, value)
    override val isFollowPreset: CSProperty<Boolean> = property(true)

    override fun value(newValue: T, fire: Boolean) {
        if (_value == newValue) return
        _value = newValue
        onValueChanged(newValue, fire)
        saveTo(store)
    }

    override var value: T
        get() = _value
        set(value) = value(value)

    override fun toString() = "key:$key ${super.toString()}"

    override fun onStoreLoaded() {
        if (isFollowPreset.isFalse)
            store.eventChanged.paused { saveTo(store) }
        else {
            update()
        }
    }

    private fun update() {
        val newValue = load()
        if (_value == newValue) return
        _value = newValue
        onValueChanged(newValue)
    }

    override fun clear() {
        store.clear(key)
        update()
    }

    private var isTrackingModified = false
    override fun trackModified(track: Boolean) = apply {
        isTrackingModified = track
    }

    override fun isModifiedIn(store: CSStore): Boolean {
        return if (isTrackingModified)
            if (store.has(key)) value != getFiltered(store)
            else value != default
        else false
    }

    val isStored get() = get(store) != null
}