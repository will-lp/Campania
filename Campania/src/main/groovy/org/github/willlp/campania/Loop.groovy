package org.github.willlp.campania

import groovy.transform.CompileStatic
import org.github.willlp.campania.event.EventManager
import org.github.willlp.campania.event.type.Game
import org.github.willlp.campania.model.timed.TimedEventManager
import org.github.willlp.campania.ui.GameView

/**
 * Created by will on 27/12/14.
 */
@CompileStatic
class Loop implements Runnable {

    static String TAG = Loop.simpleName

    GameView view
    ElementContainer container

    boolean running = true
    boolean paused = false
    Thread thread
    EventManager eventManager = EventManager.instance
    TimedEventManager timedEventManager = new TimedEventManager()


    ;{
        eventManager
                .subscribe(this)
                .to(Game.PAUSE, { paused = !paused })
    }

    Loop start() {
        thread = new Thread(this)
        thread.start()
        this
    }


    void run() {
        while(running) {

            if (paused) {
                Thread.sleep 1000
            }
            else {
                view.withCanvas { canvas ->
                    view.drawScenarioAndControls canvas
                    container.drawAll(canvas)
                }

                Thread.sleep 35
            }
        }
    }

}
