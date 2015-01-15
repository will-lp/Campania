package org.github.willlp.campania.model

import android.graphics.Color
import android.graphics.Paint
import android.util.Log
import groovy.transform.CompileStatic
import groovy.transform.EqualsAndHashCode
import org.github.willlp.campania.event.EventManager
import org.github.willlp.campania.ui.XCanvas
import org.github.willlp.campania.util.XInt

/**
 * Created by will on 27/12/14.
 */
@CompileStatic
@EqualsAndHashCode
abstract class Element {

    static String TAG = Element.simpleName

    int x
    int y
    int height = 40
    int width = 40
    int speed = 2
    EventManager eventManager = EventManager.instance

    Paint paint // FIXME: should be deleted, just while we don't have images
    Integer getImage() { null } // FIXME: should be abstract, just while we don't have images
    abstract int getColor() // FIXME: see ::color()

    def move(XCanvas canvas) {}

    def draw(XCanvas canvas) {

        if (!paint) {
            paint = new Paint(color: getColor())
        }

        if (!image) {
            canvas.drawRect(x, y, x+width, y+height, paint)
        }
        else {
            assert false, "'$this' returned an image, but not implemented yet"
        }
    }


    XInt must(int it) { new XInt(val: it) }


    boolean collided(Element that) {
        return (
                must(x).between(that.x, that.x + that.width) ||
                must(x + width).between(that.x, that.x + that.width)
        ) && (
                must(y).between(that.y, that.y + that.height) ||
                must(y + height).between(that.y, that.y + that.height) )
    }


    String toString() {
        "${getClass().simpleName}${properties}"
    }

}
