package org.github.willlp.campania.model.hero

import android.util.Log
import groovy.transform.CompileStatic
import org.github.willlp.campania.event.Event
import org.github.willlp.campania.event.type.Game
import org.github.willlp.campania.event.type.Hit
import org.github.willlp.campania.event.type.Move
import org.github.willlp.campania.model.Element
import org.github.willlp.campania.model.enemy.shot.EnemyShot
import org.github.willlp.campania.model.enemy.shot.HeroShot
import org.github.willlp.campania.model.enemy.shot.HeroShotType
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

    Move currentMove
    HeroShotType shotType = HeroShotType.EGGPLANT

    Hero() {
        eventManager.subscribe(this, Hit.HERO_HIT)
    }

    @Override
    void onEvent(Event event) {
        switch(event.type) {
            case Move.LEFT:
            case Move.RIGHT:
            case Move.STOP:
                currentMove = (Move) event.type
                break
            case Move.SHOOT:
                eventManager.raise(new Event(type: Move.SHOOT, origin: this, subject: new HeroShot(type: shotType)))
                break
            case Hit.HERO_HIT:
                if (event.subject instanceof Item) {
                    applyItem((Item) event.subject)
                } else {
                    takeHit((EnemyShot) event.subject)
                }
                break
            default:
                Log.d TAG, "Unhandled event=$event"

        }
    }

    def applyItem(Item item) {
        def map = [
            (Life): { lives++ },
            (TomatoShot) : { shotType = HeroShotType.TOMATO },
        ].withDefault { throw new RuntimeException("No handling for item $item") }
        map[item.class]()
    }

    def takeHit(EnemyShot enemyShot) {
        if ( --lives == 0 ) {
            eventManager.raise(new Event(type: Game.GAME_OVER, origin: this))
        }
    }

    @Override
    def draw(XCanvas canvas) {
        def dimension = Dimension.scenario(canvas)

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

    }

}
