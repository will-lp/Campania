package org.github.willlp.campania.model.shot

import groovy.transform.CompileStatic
import org.github.willlp.campania.ui.XCanvas

/**
 * Created by will on 28/12/14.
 */
@CompileStatic
class HeroShot extends Shot {

    def draw(XCanvas canvas) {
        y -= speed
    }

}
