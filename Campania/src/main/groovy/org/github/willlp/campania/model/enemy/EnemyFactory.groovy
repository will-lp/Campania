package org.github.willlp.campania.model.enemy

import groovy.transform.CompileStatic
import org.github.willlp.campania.model.enemy.species.BananaPeel
import org.github.willlp.campania.model.enemy.species.Crow
import org.github.willlp.campania.model.enemy.species.Fish
import org.github.willlp.campania.model.enemy.species.Fly
import org.github.willlp.campania.model.enemy.species.Rat
import org.github.willlp.campania.model.enemy.species.Thug
import org.github.willlp.campania.model.enemy.species.Tire
import org.github.willlp.campania.model.enemy.species.TrashCan
import org.github.willlp.campania.ui.Dimension
import org.github.willlp.campania.ui.XCanvas
import org.github.willlp.campania.util.XRandom

/**
 * Created by will on 01/01/15.
 */
@CompileStatic
class EnemyFactory {
    static Enemy next(int level, XCanvas canvas) {
        def enemyLevelMap = [
                (1..3) : BananaPeel,
                (4..6) : Fish,
                (7..9) : Tire,
                (10..12) : Rat,
                (13..15) : Fly,
                (16..18) : TrashCan,
                (19..21) : Crow,
                (22..24) : Thug,
        ]

        def min = Math.min(new Random().nextInt(level) + 1, 24)
        Class<? extends Enemy> enemyClazz = (Class) enemyLevelMap.findResult { it.key.contains(min) ? it.value : null }
        Enemy enemy = enemyClazz.newInstance()

        def scene = new Dimension(canvas).scenario
        enemy.y = (int) scene.top
        enemy.x = XRandom.intBetween((int) scene.left, (int) scene.right - enemy.width)

        return enemy
    }

}
