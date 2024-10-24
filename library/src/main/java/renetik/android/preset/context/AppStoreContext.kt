package renetik.android.preset.context

import renetik.android.core.lang.CSHasId
import renetik.android.event.common.CSHasDestruct
import renetik.android.store.CSStore.Companion.fileStore
import renetik.android.store.CSStore.Companion.runtimeStore
import renetik.android.store.context.StoreContext
import renetik.android.store.type.CSJsonObjectStore

fun StoreContext.Companion.empty() = CustomStoreContext(store = CSJsonObjectStore(), key = "")

fun <Parent> AppStoreContext(parent: Parent): CustomStoreContext
        where Parent : CSHasDestruct, Parent : CSHasId =
    CustomStoreContext(parent, fileStore, key = parent.id)

fun AppStoreContext(
    parent: CSHasDestruct, hasId: CSHasId? = null, key: String,
): CustomStoreContext = CustomStoreContext(parent, fileStore, key, hasId)

fun RuntimeStoreContext(
    parent: CSHasDestruct, hasId: CSHasId? = null, key: String,
): CustomStoreContext = CustomStoreContext(parent, runtimeStore, key, hasId)