package org.github.willlp.campania.event

import groovy.transform.CompileStatic
import org.github.willlp.campania.model.Element

/**
 * Created by will on 28/12/14.
 */
@CompileStatic
@Singleton
class EventManager {

    Map<EventType, List<Element>> events = [:].withDefault { [] }

    EventManager subscribe(Element element, EventType type) {
        events[type] << element
        this
    }

    EventManager raise(Event event) {
        events[event.type]*.onEvent(event)
        this
    }

}
