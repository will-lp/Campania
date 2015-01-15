package org.github.willlp.campania.util

import groovy.transform.CompileStatic
import org.github.willlp.campania.model.item.ItemType

/**
 * Created by will on 03/01/15.
 */
@CompileStatic
class RangeUtil {
    static <T> T getFromRangedKey(int value, Map<IntRange, T> map) {
        map.findResult { it.key.contains(value) ? it.value : null }
    }

    static Map<Integer, ItemType> convertToNonRangedMap(Map<IntRange, ItemType> map) {
        def intMap = [:]
        for (entry in map.entrySet()) {
            for (intKey in entry.key) {
                intMap[intKey] = entry.value
            }
        }
        intMap
    }

}
