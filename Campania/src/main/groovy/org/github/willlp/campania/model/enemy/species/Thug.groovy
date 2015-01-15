package org.github.willlp.campania.model.enemy.species

import android.graphics.Color
import android.graphics.Paint
import org.github.willlp.campania.model.enemy.Enemy

/**
 * Follows and shoots at you
 */
class Thug extends Enemy {

    @Override int getColor() { Color.rgb(0xB0, 0x00, 0xC8) } // FIXME: remove
}
