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

    static int getControlWidth(XCanvas canvas) {
        def size = canvas.screenSize
        return (canvas.landscape) ? ((int) (size.x - size.y) / 2) : size.x
    }

    static Dimension scenario(XCanvas canvas) {
        def size = canvas.screenSize

        if (canvas.landscape) {
            def controlWidth = getControlWidth(canvas)
            new Dimension(left: controlWidth + 1, top: 0, right: size.x - controlWidth - 1, bottom: size.y)
        }
        else {
            new Dimension(left: 0, top: 0, right: size.x, bottom: size.x)
        }
    }

}
