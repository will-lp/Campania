package org.github.willlp.campania.model.hero

import android.graphics.Color
import android.util.Log
import groovy.transform.CompileStatic
import org.github.willlp.campania.event.Event
import org.github.willlp.campania.event.type.Creation
import org.github.willlp.campania.event.type.Game
import org.github.willlp.campania.event.type.Hit
import org.github.willlp.campania.event.type.Move
import org.github.willlp.campania.model.Element
import org.github.willlp.campania.model.item.Item
import org.github.willlp.campania.model.item.ItemType
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

    @Override int getColor() { Color.rgb(0x12, 0x9C, 0x00) }


    ;{
        speed = 3
        eventManager
                .subscribe(this)
                .to(Hit.HERO_HIT, this.&gotHit)
                .to(Move.LEFT,    this.&setMove)
                .to(Move.RIGHT,   this.&setMove)
                .to(Move.STOP,    this.&setMove)
                .to(Move.HERO_SHOOT, this.&shoot)
    }


    def setMove(Event event) {
        currentMove = (Move) event.type
    }


    def gotHit(Event event) {
        if (event.origin instanceof Item) {
            applyItem((Item) event.origin)
        } else {
            takeHit(event)
        }
    }


    def shoot(Event event) {
        def shot = HeroShotFactory.createShot(this)
        eventManager.raise(new Event(
                type: Creation.SHOOT_CREATED,
                origin: this,
                subject: shot))
    }


    def applyItem(Item item) {
        def map = [
                (ItemType.LIFE)            : { lives++ },
                (ItemType.TOMATO_SHOT)     : { shotType = HeroShotType.TOMATO },
                (ItemType.EXTRA_SHOT)      : { maxShots = Math.min(maxShots + 1, 9) },
                (ItemType.LEMON_SHOT)      : { shotType = HeroShotType.LEMON },
                (ItemType.SPEED)           : { speed    = Math.min(speed + 1, 9) },
                (ItemType.WATERMELON_SHOT) : { shotType = HeroShotType.WATERMELON },
        ]
        map[item.type]?.call()
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
    def move(XCanvas canvas) {
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
            // well... stop moving
        }

    }

}
