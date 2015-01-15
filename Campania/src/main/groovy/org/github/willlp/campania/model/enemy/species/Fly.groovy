package org.github.willlp.campania.model.enemy.species

import android.graphics.Color
import android.graphics.Paint
import org.github.willlp.campania.model.enemy.Enemy

/**
 * Falls down randomly
 */
class Fly extends Enemy {
    int lives = 3

    @Override int getColor() { Color.DKGRAY } // FIXME: remove
}
