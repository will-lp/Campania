package org.github.willlp.campania.event

import groovy.transform.CompileStatic
import groovy.transform.ToString
import org.github.willlp.campania.model.Element
import org.github.willlp.campania.ui.XCanvas

/**
 * Created by will on 28/12/14.
 */
@CompileStatic
@ToString
class Event<T extends EventType, O, S> {
    EventType type
    O origin
    S subject
}
