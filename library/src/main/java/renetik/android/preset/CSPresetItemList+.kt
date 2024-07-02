package renetik.android.preset

import renetik.android.core.kotlin.collections.list
import renetik.android.core.kotlin.collections.putAll

val <PresetItem : CSPresetItem> CSPresetDataList<PresetItem>.items: List<PresetItem>
    get() = list(defaultItems).putAll(userItems)

val CSPresetDataList<*>.count get() = defaultItems.size + userItems.size