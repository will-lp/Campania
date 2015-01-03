package org.github.willlp.campania.event

import groovy.transform.CompileStatic
import groovy.transform.stc.ClosureParams
import groovy.transform.stc.SimpleType

/**
 * Created by will on 28/12/14.
 */
@CompileStatic
@Singleton
class EventManager {

    Map<EventType, WeakHashMap<Object, List<Closure>>> weakEvents = [:].withDefault { new WeakHashMap() }


    Subscriber subscribe(who) {
        def subs = new Subscriber()
        subs.subscriber = who
        subs
    }


    def raise(Event event) {
        weakEvents[event.type].values().each { it*.call(event) }
        this
    }


    class Subscriber {
        def subscriber
        Subscriber to(EventType type,
               @ClosureParams(value = SimpleType, options = 'org.github.willlp.campania.event.Event') Closure then) {
            if (weakEvents[type][subscriber] == null) {
                weakEvents[type][subscriber] = []
            }
            weakEvents[type][subscriber] << then
            this
        }
    }

}
