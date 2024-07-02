package renetik.android.preset

import renetik.android.core.kotlin.collections.list
import renetik.android.core.kotlin.collections.putAll

// TODO: This is little performance issue as this is creating large list in case of
//  controller presets multiple times repeatedly
val <PresetItem : CSPresetItem> CSPresetDataList<PresetItem>.items: List<PresetItem>
    get() = list(defaultItems).putAll(userItems)

val CSPresetDataList<*>.count get() = defaultItems.size + userItems.size