package renetik.android.preset.property

import renetik.android.core.lang.lazyVar
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
    var internalValue by lazyVar { load() }
    protected abstract fun get(store: CSStore): T?
    protected abstract fun set(store: CSStore, value: T)
    protected abstract fun load(): T
    protected abstract fun loadFrom(store: CSStore): T

    override var filter: ((T?) -> T?)? = null
    fun getFiltered(store: CSStore): T? = get(store).let { filter?.invoke(it) ?: it }

    override fun saveTo(store: CSStore) = set(store, value)
    override val isFollowPreset: CSProperty<Boolean> = property(true)
    override val isModified: Boolean get() = value != loadFrom(preset.item.value.store)

    override fun value(newValue: T, fire: Boolean) {
        if (internalValue == newValue) return
        internalValue = newValue
        onValueChanged(newValue, fire)
        saveTo(store)
    }

    override var value: T
        get() = internalValue
        set(value) = value(value)

    override fun toString() = "${super.toString()} key:$key value:$value"

    init {
        register(store.eventLoaded.listen { onStoreLoaded() })
    }

    private fun onStoreLoaded() {
        if (isFollowPreset.isFalse)
            presetStoreLoadedIsFollowStoreFalseSaveToParentStore()
        else {
            val newValue = load()
            if (internalValue == newValue) return
            internalValue = newValue
            onValueChanged(newValue)
        }
    }

    private fun presetStoreLoadedIsFollowStoreFalseSaveToParentStore() =
        store.eventChanged.paused { saveTo(store) } // TODO! Why ?

    private var isPresetReload = false
    private var isChangedWhilePresetReload = false

    private val presetEventReloadRegistration =
        register(preset.eventReload.listen { isPresetReload = true })

    private val presetEventAfterReloadRegistration = register(preset.eventAfterReload.listen {
        if (isChangedWhilePresetReload) super.fireChange()
        isPresetReload = false
        isChangedWhilePresetReload = false
    })

    override fun fireChange() {
        if (!isPresetReload) super.fireChange()
        else isChangedWhilePresetReload = true

    }
}