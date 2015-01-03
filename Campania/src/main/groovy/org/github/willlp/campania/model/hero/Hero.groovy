package org.github.willlp.campania.model.hero

import groovy.transform.CompileStatic
import org.github.willlp.campania.event.Event
import org.github.willlp.campania.event.type.Game
import org.github.willlp.campania.event.type.Hit
import org.github.willlp.campania.event.type.Move
import org.github.willlp.campania.model.Element
import org.github.willlp.campania.model.shot.EnemyShot
import org.github.willlp.campania.model.shot.HeroShot
import org.github.willlp.campania.model.shot.HeroShotType
import org.github.willlp.campania.model.item.Item
import org.github.willlp.campania.model.item.Life
import org.github.willlp.campania.model.item.TomatoShot
import org.github.willlp.campania.ui.Dimension
import org.github.willlp.campania.ui.XCanvas

/**
 * Created by will on 27/12/14.
 */
@CompileStatic
class Hero extends Element {

    static String TAG = Hero.simpleName
    int lives = 3
    int maxShots = 2

    Move currentMove
    HeroShotType shotType = HeroShotType.EGGPLANT

    ;{
        eventManager
                .subscribe(this)
                .to(Hit.HERO_HIT, this.&gotHit)
                .to(Move.LEFT,    this.&setMove)
                .to(Move.RIGHT,   this.&setMove)
                .to(Move.STOP,    this.&setMove)
                .to(Move.HERO_SHOOT,   this.&shoot)
    }


    def setMove(Event event) {
        currentMove = (Move) event.type
    }


    def gotHit(Event event) {
        if (event.subject instanceof Item) {
            applyItem((Item) event.subject)
        } else {
            takeHit(event)
        }
    }


    def shoot(Event event) {
        eventManager.raise(new Event(type: Move.HERO_SHOOT, origin: this, subject: new HeroShot(type: shotType)))
    }


    def applyItem(Item item) {
        def map = [
            (Life): { lives++ },
            (TomatoShot) : { shotType = HeroShotType.TOMATO },
        ].withDefault { throw new RuntimeException("No handling for item $item") }
        map[item.class]()
    }

    def takeHit(Event e) {
        shotType = HeroShotType.EGGPLANT
        if (maxShots > 2) maxShots--
        if (speed > 3) speed--
        if ( --lives == 0 ) {
            eventManager.raise(new Event(type: Game.GAME_OVER, origin: this))
        }
    }

    @Override
    def draw(XCanvas canvas) {
        def dimension = new Dimension(canvas).scenario

        if (currentMove == Move.LEFT) {
            if (x - speed < dimension.left) {
                x = (int) dimension.left
            } else {
                x -= speed
            }
        } else if (currentMove == Move.RIGHT) {
            if (x + speed + width > dimension.right) {
                x = (int) dimension.right - width
            } else {
                x += speed
            }
        }
        else if (currentMove == Move.STOP) {
            // well... stop.
        }

        super.draw canvas
    }

}
