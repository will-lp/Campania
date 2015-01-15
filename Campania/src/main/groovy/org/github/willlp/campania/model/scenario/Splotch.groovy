package org.github.willlp.campania.model.scenario

import android.graphics.Color
import android.graphics.Paint
import groovy.transform.CompileStatic
import org.github.willlp.campania.event.Event
import org.github.willlp.campania.event.type.Creation
import org.github.willlp.campania.model.Element
import org.github.willlp.campania.ui.XCanvas

/**
 * Created by will on 03/01/15.
 */
@CompileStatic
class Splotch extends Element {

    ;{
        width = 20
        height = 20
    }

    int timeToLive = 20

    def move(XCanvas canvas) {
        if (--timeToLive == 0) {
            eventManager.raise(new Event(type: Creation.SCENARIO_ELEMENT_DESTROYED, origin: this, subject: this))
        }
    }

    @Override int getColor() { Color.rgb(0xFF, 0xDA, 0x36) }

}
