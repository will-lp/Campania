package org.github.willlp.campania

import groovy.transform.CompileStatic
import org.github.willlp.campania.model.enemy.Boss
import org.github.willlp.campania.model.enemy.Enemy
import org.github.willlp.campania.model.enemy.shot.EnemyShot
import org.github.willlp.campania.model.hero.Hero
import org.github.willlp.campania.model.enemy.shot.HeroShot
import org.github.willlp.campania.ui.XCanvas

/**
 * Created by will on 28/12/14.
 */
@CompileStatic
class ElementContainer {
    List<Enemy> enemies = []
    List<HeroShot> heroShots = []
    List<EnemyShot> enemyShots = []
    Hero hero
    Boss boss

    private ElementContainer() {}

    static ElementContainer start() {
        def container = new ElementContainer(hero: new Hero())
        container
    }

    def drawAll(XCanvas canvas) {
        enemies*.draw(canvas)
        heroShots*.draw(canvas)
        enemyShots*.draw(canvas)
        hero.draw canvas
        if (boss) {
            boss.draw canvas
        }
    }
}
