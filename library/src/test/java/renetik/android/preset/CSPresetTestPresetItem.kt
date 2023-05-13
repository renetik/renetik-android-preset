package renetik.android.preset

import renetik.android.store.type.CSJsonObjectStore

class CSPresetTestItem(
    override val id: String
) : CSPresetItem {
    override val store = CSJsonObjectStore()
    override fun toString() = "${super.toString()}, id:$id"
}