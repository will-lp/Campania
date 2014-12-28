package org.github.willlp.campania.model

import android.graphics.Paint
import groovy.transform.CompileStatic
import org.github.willlp.campania.event.Event
import org.github.willlp.campania.event.EventManager
import org.github.willlp.campania.ui.XCanvas

/**
 * Created by will on 27/12/14.
 */
@CompileStatic
abstract class Element {

    int x
    int y
    int height = 20
    int width = 20
    int speed
    Paint color // FIXME: should be deleted, just while we don't have images
    EventManager eventManager = EventManager.instance

    Integer getImage() { null } // FIXME: should be abstract, just while we don't have images

    def draw(XCanvas canvas) {
        if (!image) {
            if (!color) {
                color = new Paint(color: new Random().nextInt())
            }
            canvas.drawRect(x, y, width, height, color)
        }
        else {
            assert false, "'$this' returned an image, but not implemented yet"
        }

    }

    abstract void onEvent(Event event)

    boolean collided(Element that) {

    }

}
