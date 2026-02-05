package renetik.android.preset

import renetik.android.core.lang.CSHasId

interface CSPresetItemList<PresetItem : CSPresetItem>
    : CSHasId, Collection<PresetItem> {
    companion object

    val defaultItems: List<PresetItem>
    val userItems: List<PresetItem>
    fun remove(item: PresetItem)
    fun createItem(title: String, isDefault: Boolean): PresetItem
    fun reload()

    //Collection
    override val size: Int get() = defaultItems.size + userItems.size
    override fun isEmpty(): Boolean = defaultItems.isEmpty() && userItems.isEmpty()
    override fun contains(element: PresetItem): Boolean =
        defaultItems.contains(element) || userItems.contains(element)

    override fun containsAll(elements: Collection<PresetItem>): Boolean =
        elements.all { contains(it) }

    override fun iterator(): Iterator<PresetItem> = object : Iterator<PresetItem> {
        private val first = defaultItems.iterator()
        private val second = userItems.iterator()
        override fun hasNext(): Boolean = first.hasNext() || second.hasNext()
        override fun next(): PresetItem =
            if (first.hasNext()) first.next() else second.next()
    }

    fun find(predicate: (PresetItem) -> Boolean): PresetItem? =
        defaultItems.find(predicate) ?: userItems.find(predicate)

    operator fun get(index: Int): PresetItem {
        val splitIndex = defaultItems.size
        return if (index < splitIndex) defaultItems[index]
        else userItems[index - splitIndex]
    }

    fun at(index: Int): PresetItem? = if (index in indices) get(index) else null

    fun index(item: PresetItem): Int? {
        val defaultIndex = defaultItems.indexOf(item)
        if (defaultIndex >= 0) return defaultIndex
        val userIndex = userItems.indexOf(item)
        if (userIndex >= 0) return defaultItems.size + userIndex
        return null
    }
}