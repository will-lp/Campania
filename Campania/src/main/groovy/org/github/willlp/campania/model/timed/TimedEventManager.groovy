package org.github.willlp.campania.model.timed

import groovy.transform.CompileStatic
import org.github.willlp.campania.event.Event
import org.github.willlp.campania.event.EventManager
import org.github.willlp.campania.event.type.Creation
import org.github.willlp.campania.event.type.Hit
import org.github.willlp.campania.model.item.Item
import org.github.willlp.campania.model.item.ItemType

/**
 * Created by will on 04/01/15.
 */
@CompileStatic
class TimedEventManager {

    EventManager eventManager = EventManager.instance

    ;{
        eventManager
                .subscribe(this)
                .to(Hit.HERO_HIT, this.&createSlowdownEvent)
    }

    def createSlowdownEvent(Event event) {
        if (event.origin instanceof Item) {
            def item = (Item) event.origin
            if (item.type == ItemType.SLOWDOWN) {
                eventManager.raise(new Event(
                        type: Creation.TIMED_EVENT_CREATED,
                        origin: this,
                        subject: new SlowdownTimed()))
            }
        }
    }

}
