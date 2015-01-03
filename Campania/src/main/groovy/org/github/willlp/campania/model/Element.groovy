package org.github.willlp.campania.model

import android.graphics.Paint
import groovy.transform.CompileStatic
import groovy.transform.ToString
import org.github.willlp.campania.event.EventManager
import org.github.willlp.campania.ui.XCanvas

/**
 * Created by will on 27/12/14.
 */
@CompileStatic
abstract class Element {

    int x
    int y
    int height = 30
    int width = 30
    int speed = 1
    EventManager eventManager = EventManager.instance

    Paint color // FIXME: should be deleted, just while we don't have images
    Integer getImage() { null } // FIXME: should be abstract, just while we don't have images


    def draw(XCanvas canvas) {
        if (!image) {
            if (!color) {
                color = new Paint(color: new Random().nextInt())
            }
            canvas.drawRect(x, y, x+width, y+height, color)
        }
        else {
            assert false, "'$this' returned an image, but not implemented yet"
        }

    }

    boolean collided(Element that) {
        if ( that.x >= x && that.x <= x + width) {
            return that.y >= y && that.y <= y + height
        }
    }

    String toString() {
        "${getClass().simpleName}${properties}"
    }


}
