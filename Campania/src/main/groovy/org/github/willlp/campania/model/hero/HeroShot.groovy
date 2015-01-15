package org.github.willlp.campania.model.hero

import android.graphics.Color
import groovy.transform.CompileStatic
import org.github.willlp.campania.model.shot.Shot
import org.github.willlp.campania.ui.XCanvas

/**
 * Created by will on 28/12/14.
 */
@CompileStatic
abstract class HeroShot extends Shot {

    def move(XCanvas canvas) {
        y -= speed
    }

}
