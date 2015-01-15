package org.github.willlp.campania.model.enemy

import groovy.transform.CompileStatic
import org.github.willlp.campania.event.Event
import org.github.willlp.campania.event.type.Creation
import org.github.willlp.campania.event.type.Hit
import org.github.willlp.campania.event.type.Score
import org.github.willlp.campania.model.Element
import org.github.willlp.campania.ui.XCanvas

/**
 * Created by will on 27/12/14.
 */
@CompileStatic
abstract class Enemy extends Element {

    int startingLives = 1
    int lives = startingLives;

    ;{
        eventManager.subscribe(this)
                .to(Hit.ENEMY_HIT, this.&enemyHit)

    }


    def move(XCanvas canvas) {
        y += speed
    }


    def enemyHit(Event event) {
        if (event.subject == this) {
            if (--lives == 0) {
                eventManager.raise(new Event(type: Creation.ENEMY_DESTROYED, origin: event.origin, subject: this))
            }
        }
    }

}
