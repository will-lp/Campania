package org.github.willlp.campania.event

import groovy.transform.CompileStatic
import groovy.transform.stc.ClosureParams
import groovy.transform.stc.FirstParam
import groovy.transform.stc.SimpleType
import org.github.willlp.campania.model.Element

/**
 * Created by will on 28/12/14.
 */
@CompileStatic
@Singleton
class EventManager {

    Map<EventType, List<Closure>> events = [:].withDefault { [] }

    EventManager subscribe(
            EventType type,
            @ClosureParams(value=SimpleType, options='org.github.willlp.campania.event.Event') Closure then) {
        events[type] << then
        this
    }


    EventManager raise(Event event) {
        events[event.type]*.call(event)
        this
    }

}
