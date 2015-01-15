package org.github.willlp.campania.model.item

import android.graphics.Color
import android.graphics.Paint
import groovy.transform.CompileStatic
import org.github.willlp.campania.event.Event
import org.github.willlp.campania.model.Element
import org.github.willlp.campania.ui.Dimension
import org.github.willlp.campania.ui.XCanvas

/**
 * Created by will on 28/12/14.
 */
@CompileStatic
class Item extends Element {

    ;{
        height = 24
        width = height
        speed = 2
    }

    ItemType type

    @Override def move(XCanvas canvas) { y += speed }

    @Override int getColor() {
        [
                (ItemType.EXTRA_SHOT)  : Color.rgb(0xFF, 0x91, 0x00),
                (ItemType.LEMON_SHOT)  : Color.rgb(0xD9, 0xFF, 0x00),
                (ItemType.LIFE)        : Color.rgb(0xFF, 0x99, 0xF1),
                (ItemType.SLOWDOWN)    : Color.LTGRAY,
                (ItemType.SPEED)       : Color.BLUE,
                (ItemType.TOMATO_SHOT) : Color.RED,
                (ItemType.WATERMELON_SHOT) : Color.GREEN,
        ][ type ]
    }

    @Override def draw(XCanvas canvas) { // FIXME: remove me
        def radius = (int) width / 2
        canvas.drawCircle(x + radius, y + radius, radius, new Paint(color: getColor()))
    }

}
