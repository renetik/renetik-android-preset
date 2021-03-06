package renetik.android.preset.property

import renetik.android.core.lang.variable.isFalse
import renetik.android.event.common.CSHasRegistrationsHasDestroy
import renetik.android.event.paused
import renetik.android.event.property.CSProperty
import renetik.android.event.property.CSProperty.Companion.property
import renetik.android.event.property.CSPropertyBase
import renetik.android.event.registration.register
import renetik.android.preset.CSPreset
import renetik.android.store.CSStore

abstract class CSPresetPropertyBase<T>(
    override val parent: CSHasRegistrationsHasDestroy,
    final override val preset: CSPreset<*, *>,
    override val key: String,
    onChange: ((value: T) -> Unit)? = null
) : CSPropertyBase<T>(parent, onChange), CSPresetProperty<T> {

    protected abstract val default: T
    protected abstract var _value: T
    protected abstract fun get(store: CSStore): T?
    protected abstract fun set(store: CSStore, value: T)
    protected abstract fun load(): T
    protected abstract fun loadFrom(store: CSStore): T

    override fun saveTo(store: CSStore) = set(store, value)
    override val isFollowPreset: CSProperty<Boolean> = property(true)
    override val isModified: Boolean get() = value != loadFrom(preset.item.value.store)

    private val storeLoadedRegistration =
        register(store.eventLoaded.listen { onStoreLoaded() })

    private fun onStoreLoaded() {
        if (isFollowPreset.isFalse)
            presetStoreLoadedIsFollowStoreFalseSaveToParentStore()
        else {
            val newValue = load()
            if (_value == newValue) return
            _value = newValue
//            storeLoadedRegistration.paused {
            onValueChanged(newValue)
//            }
        }
    }

    private fun presetStoreLoadedIsFollowStoreFalseSaveToParentStore() =
        store.eventChanged.paused { saveTo(store) } /// TODO Why ?

    override fun value(newValue: T, fire: Boolean) {
        if (_value == newValue) return
        _value = newValue
//        storeLoadedRegistration.paused {
        onValueChanged(newValue, fire)
        saveTo(store)
//        }
    }

    override var value: T
        get() = _value
        set(value) = value(value)

    override fun toString() = "${super.toString()} key:$key value:$value"
}