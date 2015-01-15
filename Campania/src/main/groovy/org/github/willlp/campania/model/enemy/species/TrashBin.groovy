package org.github.willlp.campania.model.enemy.species

import android.graphics.Color
import android.graphics.Paint
import org.github.willlp.campania.model.enemy.Enemy

/**
 * Sticks to the top, shoots some garbage and then falls down
 */
class TrashBin extends Enemy {
    int lives = 10

    @Override int getColor() { Color.BLUE } // FIXME: remove
}
