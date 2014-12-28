package org.github.willlp.campania.ui

import groovy.transform.CompileStatic

/**
 * Created by will on 28/12/14.
 */
@CompileStatic
class Dimension {

    float top
    float left
    float right
    float bottom

    static Dimension scenario(XCanvas canvas) {
        def size = canvas.screenSize

        if (canvas.landscape) {
            def controlWidth = (int) size.x * 0.3f
            new Dimension(left: controlWidth + 1, top: 0, right: size.x - controlWidth - 1, bottom: size.y)
        }
        else {
            new Dimension(left: 0, top: 0, right: size.x, bottom: size.x)
        }
    }

}
