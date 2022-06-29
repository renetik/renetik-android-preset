package renetik.android.preset

import renetik.android.core.kotlin.collections.list
import renetik.android.core.kotlin.collections.putAll

val <PresetItem : CSPresetItem> CSPresetItemList<PresetItem>.items
    get() = list(defaultItems).putAll(userItems)

val CSPresetItemList<*>.count get() = defaultItems.size + userItems.size