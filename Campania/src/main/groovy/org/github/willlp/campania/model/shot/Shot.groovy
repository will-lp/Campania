package org.github.willlp.campania.model.shot

import android.graphics.Color
import android.graphics.Paint
import org.github.willlp.campania.model.Element
import org.github.willlp.campania.model.item.ItemType

/**
 * Created by will on 27/12/14.
 */
abstract class Shot extends Element {
    int damage = 1
//    ShotType type

    ;{
        speed  = 5
        width  = 10
        height = 10
    }

}
