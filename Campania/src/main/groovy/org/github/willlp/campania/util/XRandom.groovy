package org.github.willlp.campania.util

import groovy.transform.CompileStatic

/**
 * Created by will on 01/01/15.
 */
@CompileStatic
class XRandom {
    static int intBetween(int min, int max) {
        new Random().nextInt(max - min) + min
    }
}
