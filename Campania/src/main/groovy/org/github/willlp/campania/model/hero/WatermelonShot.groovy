package org.github.willlp.campania.model.hero

import android.graphics.Color
import groovy.transform.CompileStatic
import org.github.willlp.campania.event.Event
import org.github.willlp.campania.event.type.Creation
import org.github.willlp.campania.model.Element

/**
 * Created by will on 10/01/15.
 */
@CompileStatic
class WatermelonShot extends HeroShot {

    ;{
        width = 30
        height = 14

    }

    @Override boolean collided(Element that) {
        if (super.collided(that)) {
            def shrapnels = [
                    new WatermelonShrapnelShot(
                            x: x,
                            y: y,
                            left: false),
                    new WatermelonShrapnelShot(
                            x: x,
                            y: y,
                            left: true)
            ]

            eventManager.raise(new Event(
                    type: Creation.SHOOT_CREATED,
                    origin: this,
                    subject: shrapnels)
                )
        }
    }

    @Override int getColor() { Color.GREEN }

}
