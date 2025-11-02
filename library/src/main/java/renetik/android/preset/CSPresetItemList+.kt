package renetik.android.preset

import renetik.android.core.kotlin.collections.mutable
import renetik.android.core.kotlin.collections.putAll

val CSPresetItemList<*>.size: Int get() = defaultItems.size + userItems.size

// TODO: This is little performance issue as this is creating large list in case of
//  controller presets multiple times repeatedly
val <PresetItem : CSPresetItem> CSPresetItemList<PresetItem>.items: List<PresetItem>
    get() = defaultItems.mutable().putAll(userItems)

val CSPresetItemList<*>.count get() = defaultItems.size + userItems.size