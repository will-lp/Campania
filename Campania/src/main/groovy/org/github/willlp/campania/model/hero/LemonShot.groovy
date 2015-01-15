package org.github.willlp.campania.model.hero

import android.graphics.Color
import groovy.transform.CompileStatic
import org.github.willlp.campania.ui.XCanvas

/**
 * Created by will on 10/01/15.
 */
@CompileStatic
class LemonShot extends HeroShot {
    boolean left

    int loops = 0

    @Override def move(XCanvas canvas) {
        if (++loops % 2 == 0) {
            if (left) {
                x -= 1
            } else {
                x += 1
            }
        }
        y -= speed
    }

    @Override int getColor() { Color.YELLOW }
}
