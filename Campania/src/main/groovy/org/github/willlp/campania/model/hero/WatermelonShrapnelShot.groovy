package org.github.willlp.campania.model.hero

import android.graphics.Color
import groovy.transform.CompileStatic
import org.github.willlp.campania.event.Event
import org.github.willlp.campania.event.type.Creation
import org.github.willlp.campania.ui.XCanvas

/**
 * Created by will on 10/01/15.
 */
@CompileStatic
class WatermelonShrapnelShot extends HeroShot {
    boolean left

    int timeToLive = 30

    ;{
        height = 11
        width = 11
    }

    @Override def move(XCanvas canvas) {
        if (--timeToLive == 0) {
            eventManager.raise new Event(
                    type: Creation.SHOOT_DESTROYED,
                    origin: this,
                    subject: this)
        }
        else {
            if (left) {
                x -= speed
            }
            else {
                x += speed
            }
        }
    }

    @Override int getColor() { Color.GREEN }
}
