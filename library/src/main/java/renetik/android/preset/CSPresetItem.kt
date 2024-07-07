package renetik.android.preset

import renetik.android.core.lang.CSHasId
import renetik.android.store.CSStore

interface CSPresetItem : CSHasId {
    val store: CSStore
}