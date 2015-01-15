package org.github.willlp.campania.model.hero

import groovy.transform.CompileStatic

/**
 * Created by will on 03/01/15.
 */
@CompileStatic
class HeroShotFactory {
    static def createShot(Hero hero) {

        return [
                (HeroShotType.EGGPLANT)   : {
                    new EggplantShot(
                            x: (int) hero.x + hero.width / 2,
                            y: hero.y,
                    )
                },
                (HeroShotType.LEMON)      : {
                    [
                            new LemonShot(
                                    x: (int) hero.x + (hero.width / 3),
                                    y: hero.y,
                                    left: true),
                            new LemonShot(
                                    x: (int) hero.x + (hero.width / 3 * 2),
                                    y: hero.y,
                                    left: false)

                    ]
                },
                (HeroShotType.TOMATO)     : {
                    [
                            new TomatoShot(
                                    x: (int) hero.x + (hero.width / 3),
                                    y: hero.y),
                            new TomatoShot(
                                    x: (int) hero.x + (hero.width / 3 * 2),
                                    y: hero.y)
                    ]
                },
                (HeroShotType.WATERMELON) : {
                    new WatermelonShot(
                            x: hero.x,
                            y: hero.y
                    )
                },

        ][hero.shotType]()


    }
}
