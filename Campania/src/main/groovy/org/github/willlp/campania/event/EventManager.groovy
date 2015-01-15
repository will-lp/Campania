package org.github.willlp.campania.event

import android.util.Log
import groovy.transform.CompileStatic
import groovy.transform.stc.ClosureParams
import groovy.transform.stc.SimpleType
import org.github.willlp.campania.event.type.Creation
import org.github.willlp.campania.event.type.Game

/**
 * Created by will on 28/12/14.
 */
@CompileStatic
@Singleton
class EventManager {

    static String TAG = EventManager.simpleName

//    Map<EventType, WeakHashMap<Object , List<Closure>>> subscribers = [:].withDefault { new WeakHashMap() }
    Map<EventType, WeakHashMap<Object , Closure>> subscribers = [:].withDefault { new WeakHashMap() }
//    Map<Object, Map<EventType, Closure>> subscribers = new WeakHashMap<>()

    ;{
        subscribe(this)
            .to(Game.ELEMENT_REMOVED, { event -> subscribers.each { it.value.remove(event.subject) } })
    }

    Subscriber subscribe(who) {
        def subs = new Subscriber()
        subs.subscriber = who
        subs
    }


    def raise(Event event) {
        subscribers[event.type].values()*.call(event)
        this
    }


//    def raise(Event event) {
//        Log.d TAG, "raise subscribers=${subscribers.entrySet().join('\n')}"
//        for (entry in subscribers.entrySet()) {
//            entry.value[event.type]?.call(event)
//        }
//    }

    def raiseAll(List<Event> events) {
        for (event in events) {
            raise event
        }
    }


    class Subscriber {
        def subscriber
//        Subscriber to(EventType type,
//               @ClosureParams(value = SimpleType, options = 'org.github.willlp.campania.event.Event') Closure then) {
//            if (subscribers[type][subscriber] == null) {
//                subscribers[type][subscriber] = []
//            }
//            subscribers[type][subscriber] << then
//            this
//        }

        Subscriber to(EventType type,
                      @ClosureParams(value = SimpleType, options = 'org.github.willlp.campania.event.Event') Closure then) {
            if (subscribers[type][subscriber]) {
                assert false, "$subscriber already subscribed to $type"
            }
            subscribers[type][subscriber] = then
            this
        }

//        Subscriber to(EventType type,
//                      @ClosureParams(value = SimpleType, options = 'org.github.willlp.campania.event.Event') Closure then) {
//            if (subscribers[subscriber] == null) {
//                subscribers[subscriber] = [ : ]
//            }
//            subscribers[subscriber][type] = then
//            this
//        }

    }

}
