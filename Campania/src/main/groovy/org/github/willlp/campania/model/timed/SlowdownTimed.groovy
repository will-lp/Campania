package org.github.willlp.campania.model.timed

import groovy.transform.CompileStatic
import org.github.willlp.campania.ElementContainer
import org.github.willlp.campania.event.Event
import org.github.willlp.campania.event.EventManager
import org.github.willlp.campania.event.type.Creation
import org.github.willlp.campania.model.enemy.Enemy

/**
 * Created by will on 04/01/15.
 */
@CompileStatic
class SlowdownTimed implements Timed {

    int loops = 200

    EventManager eventManager = EventManager.instance
    Map<Enemy, Integer> oldSpeed = [:]

    @Override
    def act(ElementContainer container) {

        for (enemy in container.enemies) {
            if (!oldSpeed.containsKey(enemy)) {
                oldSpeed[enemy] = enemy.speed
                enemy.speed = 1
            }
        }

        if (--loops == 0) {
            for (enemy in container.enemies) {
                if (oldSpeed.containsKey(enemy)) {
                    enemy.speed = oldSpeed[enemy]
                }
            }
            eventManager.raise(new Event(type: Creation.TIMED_EVENT_DESTROYED, origin: this))
        }
    }
}
