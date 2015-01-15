package org.github.willlp.campania.ui

import groovy.transform.CompileStatic
import groovy.transform.ToString
import org.github.willlp.campania.model.Element

/**
 * Created by will on 28/12/14.
 */
@CompileStatic
@ToString(includeNames = true, includePackage = false)
class Dimension {

    float top
    float left
    float right
    float bottom
    private XCanvas canvas

    static int cachedLandscapeControlWidth = -1

    Dimension(){}

    Dimension(XCanvas canvas) { this.canvas = canvas}

    int getLandscapeControlWidth() {
        if (cachedLandscapeControlWidth == -1) {
            def size = canvas.screenSize
            def controls = (canvas.landscape) ? size.y : size.x
            cachedLandscapeControlWidth = (int) controls / 2
        }
        return cachedLandscapeControlWidth
    }

    int getControlPanelWidth() {
        def size = canvas.screenSize
        return (canvas.landscape) ? landscapeControlWidth : size.x
    }


    Dimension getScenario() {
        def size = canvas.screenSize

        if (canvas.landscape) {
            def controlWidth = getControlPanelWidth()
            new Dimension(left: controlWidth + 1, top: 0, right: size.x - controlWidth - 1, bottom: size.y)
        }
        else {
            new Dimension(left: 0, top: 0, right: size.x, bottom: size.x)
        }
    }


    boolean outOfScenario(Element e) {
        def scenario = getScenario()
        return  (e.x + e.width < scenario.left)  ||
                (e.x > scenario.right) ||
                (e.y > scenario.bottom) ||
                (e.y + e.height < scenario.top)
    }

    boolean contains(float x, float y) {
        return (x >= left && x <= right) && (y >= top && y <= bottom)
    }

}
