package org.github.willlp.campania

import android.util.Log
import groovy.transform.CompileStatic
import org.github.willlp.campania.event.Event
import org.github.willlp.campania.event.EventManager
import org.github.willlp.campania.event.type.Creation
import org.github.willlp.campania.event.type.Game
import org.github.willlp.campania.event.type.Hit
import org.github.willlp.campania.model.Element
import org.github.willlp.campania.model.GameStatus
import org.github.willlp.campania.model.enemy.Boss
import org.github.willlp.campania.model.enemy.Enemy
import org.github.willlp.campania.model.enemy.EnemyFactory
import org.github.willlp.campania.model.hero.*
import org.github.willlp.campania.model.item.Item
import org.github.willlp.campania.model.item.ItemFactory
import org.github.willlp.campania.model.scenario.Splotch
import org.github.willlp.campania.model.shot.EnemyShot
import org.github.willlp.campania.model.shot.Shot
import org.github.willlp.campania.model.timed.Timed
import org.github.willlp.campania.ui.Dimension
import org.github.willlp.campania.ui.XCanvas
import org.github.willlp.campania.util.XRandom

/**
 * Created by will on 28/12/14.
 */
@CompileStatic
class ElementContainer {

    static String TAG = ElementContainer.simpleName

    List<Enemy> enemies = []
    List<HeroShot> heroShots = []
    List<EnemyShot> enemyShots = []
    List<Element> scenarioStuff = []
    List<Item> items = []
    List<Timed> timedEvents = []
    Hero hero
    Boss boss
    GameStatus gameStatus
    EventManager eventManager = EventManager.instance
    int loops = 0


    ;{
        eventManager
                .subscribe(this)
                .to(Creation.ENEMY_DESTROYED, this.&enemyDestroyed)
                .to(Creation.SHOOT_CREATED,   this.&heroShot)
                .to(Creation.SCENARIO_ELEMENT_DESTROYED, { scenarioStuff.remove(it.origin) })
                .to(Creation.ITEM_CREATED,   { items << (Item) it.subject })
                .to(Hit.HERO_HIT,  this.&removeWhatHitHero)
                .to(Hit.ENEMY_HIT, { assert it.origin instanceof Shot; heroShots.remove(it.origin) })
                .to(Creation.TIMED_EVENT_CREATED, { timedEvents << (Timed) it.subject })
                .to(Creation.TIMED_EVENT_DESTROYED, { timedEvents.remove(it.origin) })
                .to(Creation.SHOOT_DESTROYED, { heroShots -= it.origin })
    }


    static ElementContainer start(XCanvas canvas) {
        def scenario = new Dimension(canvas).scenario

        def hero = new Hero()
        hero.x = (int) ((scenario.right - scenario.left) / 2) + scenario.left - hero.width / 2
        hero.y = (int) scenario.bottom - hero.height

        Log.d TAG, "scenario.left=$scenario.left, scenario.right=$scenario.right, hero.x=$hero.x"
        Log.d TAG, "hero=$hero, scenario=$scenario, canvas.screenSize=$canvas.screenSize"

        return new ElementContainer(
                hero: hero,
                gameStatus: new GameStatus(),
        )
    }


    def canAddShoots(Event e) {
        def shot = e.subject
        if (shot instanceof List) {
            def firstShot = shot.head()
            if (firstShot instanceof LemonShot || firstShot instanceof TomatoShot) {
                heroShots.size() - 1 < hero.maxShots
            }
            else if (firstShot instanceof WatermelonShrapnelShot) {
                true
            }
        }
        else if (shot instanceof WatermelonShot) {
            heroShots.count { !(it instanceof WatermelonShrapnelShot) } < hero.maxShots
        }
        else {
            heroShots.size() < hero.maxShots
        }
    }


    def heroShot( Event<Creation, ?, ?> e ) {
        if (canAddShoots(e)) {
            if (e.subject instanceof HeroShot) {
                heroShots << (HeroShot) e.subject
            }
            if (e.subject instanceof List) {
                heroShots.addAll( (List) e.subject )
            }
        }
    }


    def enemyDestroyed(Event<Creation, Shot, Enemy> event) {
        enemies.remove(event.subject)
        scenarioStuff << new Splotch(x: event.origin.x, y: event.origin.y)
    }


    def removeWhatHitHero(Event event) {
        if (event.origin instanceof Shot) {
            enemyShots.remove(event.origin)
        } else if(event.origin instanceof Enemy) {
            enemies.remove(event.origin)
        } else if (event.origin instanceof Item) {
            items.remove(event.origin)
        }
    }


    List<List<Element>> getElementsList() {
        [enemies, heroShots, enemyShots, items, scenarioStuff]
    }


    List<Element> getAllElements() {
        [enemies, heroShots, enemyShots, items, hero, boss, scenarioStuff].flatten().findAll()
    }


    def drawAll(XCanvas canvas) {
        loops++
        def all = getAllElements()
        all*.move(canvas)
        all*.draw(canvas)
        timedEvents*.act(this)
        addEnemies(canvas)
        checkCollisions()
        removeOutOfBounds(canvas)
        checkBonusChance(canvas)
    }


    def checkCollisions() {
        for (enemy in enemies) {
            if (enemy.collided(hero)) {
                eventManager.raise new Event(type: Hit.HERO_HIT, origin: enemy, subject: hero)
            }
        }


        for (shot in heroShots) {
            for (enemy in enemies) {
                if (shot.collided(enemy)) {
                    eventManager.raise new Event(type: Hit.ENEMY_HIT, origin: shot, subject: enemy)
                }
            }
        }


        for (shot in enemyShots) {
            if (shot.collided(hero)) {
                eventManager.raise new Event(type: Hit.HERO_HIT, origin: shot, subject: hero)
            }
        }


        for (item in items) {
            if (item.collided(hero)) {
                eventManager.raise new Event(type: Hit.HERO_HIT, origin: item, subject: hero)
            }
        }
    }


    def addEnemies(XCanvas canvas) {
        if (loops % 24 == 0) {
            enemies.add(EnemyFactory.next(gameStatus.level, canvas))
        }
    }


    def removeOutOfBounds(XCanvas canvas) {
        def dimension = new Dimension(canvas)

        for (list in elementsList) {
            def outOfBounds = list.findAll { e -> dimension.outOfScenario e }
            list.removeAll outOfBounds
            eventManager.raiseAll outOfBounds.collect { new Event(type: Game.ELEMENT_REMOVED, origin: this, subject: it) }
        }

    }

    def checkBonusChance(XCanvas canvas) {
        if (XRandom.nextInt(200) == 1) {
            eventManager.raise(new Event(
                    type: Creation.ITEM_CREATED,
                    subject: ItemFactory.create(canvas),
                    origin: this))
        }
    }

}
