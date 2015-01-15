package org.github.willlp.campania.model.item

import android.util.Log
import groovy.transform.CompileStatic
import org.github.willlp.campania.ui.Dimension
import org.github.willlp.campania.ui.XCanvas
import org.github.willlp.campania.util.RangeUtil
import org.github.willlp.campania.util.XRandom

/**
 * Created by will on 03/01/15.
 */
@CompileStatic
class ItemFactory {

    static String TAG = ItemFactory.simpleName

    static Map<Integer, ItemType> map = RangeUtil.convertToNonRangedMap([
            (1..4)   : ItemType.SPEED,
            (5..6)   : ItemType.EXTRA_SHOT,
            (7..8)   : ItemType.WATERMELON_SHOT,
            (9..10)  : ItemType.LEMON_SHOT,
            (11..12) : ItemType.TOMATO_SHOT,
            (13..13) : ItemType.LIFE,
            (14..14) : ItemType.SLOWDOWN
    ])


    static Item create(XCanvas canvas) {
        int itemCode = XRandom.intBetween(1, 14)
        def type = map[itemCode]

        def item = new Item(type: type, y: 0)

        def scene = new Dimension(canvas).scenario
        item.x = XRandom.intBetween((int) scene.left + item.width, (int) scene.right - item.width)

        return item
    }
}
