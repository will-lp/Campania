package org.github.willlp.campania.event

import android.util.Log
import groovy.transform.CompileStatic

/**
 * Created by will on 28/12/14.
 */
@CompileStatic
trait EventType {
    abstract String name()
}
