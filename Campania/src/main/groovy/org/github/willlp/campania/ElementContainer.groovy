package org.github.willlp.campania

import groovy.transform.CompileStatic
import org.github.willlp.campania.event.Event
import org.github.willlp.campania.event.EventManager
import org.github.willlp.campania.event.type.Creation
import org.github.willlp.campania.event.type.Hit
import org.github.willlp.campania.model.GameStatus
import org.github.willlp.campania.model.enemy.Boss
import org.github.willlp.campania.model.enemy.Enemy
import org.github.willlp.campania.model.enemy.EnemyFactory
import org.github.willlp.campania.model.enemy.shot.EnemyShot
import org.github.willlp.campania.model.enemy.shot.HeroShot
import org.github.willlp.campania.model.enemy.shot.Shot
import org.github.willlp.campania.model.hero.Hero
import org.github.willlp.campania.ui.Dimension
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
    GameStatus gameStatus
    EventManager eventManager = EventManager.instance
    int loops = 0

    ;{
        eventManager
                .subscribe(Creation.ENEMY_DESTROYED, { enemies.remove(it.subject) })
                .subscribe(Hit.HERO_HIT, this.&removeWhatHitHero)
                .subscribe(Hit.ENEMY_HIT, { assert it.origin instanceof Shot; heroShots.remove(it.origin) })
    }


    static ElementContainer start(XCanvas canvas) {
        def scenario = Dimension.scenario(canvas)

        def hero = new Hero()
        hero.x = (int) scenario.left - (int)(scenario.right / 2)
        hero.y = (int) scenario.bottom - hero.height

        def container = new ElementContainer(hero: hero, gameStatus: new GameStatus())
        container
    }


    def removeWhatHitHero(Event event) {
        if (event.origin instanceof Shot) {
            enemyShots.remove(event.origin)
        } else if(event.origin instanceof Enemy) {
            enemies.remove(event.origin)
        } else {
            // no idea, maybe a boss hit him
        }
    }


    def drawAll(XCanvas canvas) {
        loops++
        enemies*.draw(canvas)
        heroShots*.draw(canvas)
        enemyShots*.draw(canvas)
        hero.draw canvas
        if (boss) {
            boss.draw canvas
        }
        addEnemies(canvas)
        checkCollisions()
    }


    def checkCollisions() {
        for (enemy in enemies) {
            if (enemy.collided(hero)) {
                eventManager.raise( new Event(type: Hit.HERO_HIT, origin: enemy, subject: hero) )
            }
        }

        for (shot in heroShots) {
            for (enemy in enemies) {
                if (shot.collided(enemy)) {
                    eventManager.raise(new Event(type: Hit.ENEMY_HIT, origin: shot, subject: enemy))
                }
            }
        }

        for (shot in enemyShots) {
            if (shot.collided(hero)) {
                eventManager.raise(new Event(type: Hit.HERO_HIT, origin: shot, subject: hero))
            }
        }
    }


    def addEnemies(XCanvas canvas) {
        if (loops % 4 == 0) {
            enemies.add(EnemyFactory.next(gameStatus.level, canvas))
        }
    }


}
