package org.github.willlp.campania.model

import groovy.transform.CompileStatic
import org.github.willlp.campania.event.Event
import org.github.willlp.campania.event.EventManager
import org.github.willlp.campania.event.type.Hit
import org.github.willlp.campania.event.type.Score

/**
 * Created by will on 01/01/15.
 */
@CompileStatic
class GameStatus {
    int score = 0
    int level = 1
    EventManager eventManager = EventManager.instance

    ;{
        eventManager
                .subscribe(Hit.ENEMY_HIT, this.&enemyHit)
    }

    def enemyHit(Event event) {
        score += 10
        if ( score % 250 == 0 ) {
            level++
            eventManager.raise(new Event(type: Score.LEVEL_UP, origin: this, subject: score))
        }
    }
}
