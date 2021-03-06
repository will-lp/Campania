package org.github.willlp.campania.model.shot

import android.graphics.Color
import android.graphics.Paint
import groovy.transform.CompileStatic
import org.github.willlp.campania.ui.XCanvas

/**
 * Created by will on 28/12/14.
 */
@CompileStatic
class EnemyShot extends Shot {
    def move(XCanvas canvas) {
        y += speed
    }

    @Override int getColor() { Color.RED } // FIXME: remove
}
