package org.github.willlp.campania.model.enemy

import groovy.transform.CompileStatic
import org.github.willlp.campania.event.Event
import org.github.willlp.campania.event.type.Creation
import org.github.willlp.campania.event.type.Hit
import org.github.willlp.campania.model.Element

/**
 * Created by will on 27/12/14.
 */
@CompileStatic
abstract class Enemy extends Element {

    int lives = 1

    Enemy() {
        eventManager.subscribe(this, Hit.ENEMY_HIT)
    }

    void onEvent(Event event) {
        if (event.type == Hit.ENEMY_HIT && event.subject == this) {
            if (--lives == 0) {
                eventManager.raise(new Event(type: Creation.ENEMY_DESTROYED, origin: this))
            }
        }
    }
}
