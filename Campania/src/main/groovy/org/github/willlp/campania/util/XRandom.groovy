package org.github.willlp.campania.util

import groovy.transform.CompileStatic

/**
 * Created by will on 01/01/15.
 */
@CompileStatic
class XRandom {
    static Random r = new Random()
    static int intBetween(int min, int max) {
        r.nextInt(max - min + 1) + min
    }

    static int intBetween(float min, float max) {
        intBetween((int) min, (int) max)
    }

    static int nextInt(int max) {
        r.nextInt(max)
    }
}
