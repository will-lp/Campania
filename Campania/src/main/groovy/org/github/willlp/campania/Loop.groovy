package org.github.willlp.campania

import android.graphics.Canvas
import android.util.Log
import groovy.transform.CompileStatic
import org.github.willlp.campania.event.EventManager
import org.github.willlp.campania.model.Element
import org.github.willlp.campania.ui.GameView
import org.github.willlp.campania.ui.XCanvas

/**
 * Created by will on 27/12/14.
 */
@CompileStatic
class Loop implements Runnable {

    static String TAG = Loop.simpleName

    GameView view
    boolean running = true
    Thread thread
    ElementContainer elements
    long timer = 0l
    EventManager eventManager = EventManager.instance


    def start() {
        elements = ElementContainer.start()

        thread = new Thread(this)
        thread.start()
    }


    void run() {
        while(running) {

            withCanvas { XCanvas canvas ->
                view.draw canvas
                elements.drawAll(canvas)
            }

            Thread.sleep 500
        }
    }


    def withCanvas(Closure closure) {
        def canvas
        try {
            canvas = view.holder.lockCanvas()
            if (canvas) {
                def xcanvas = new XCanvas(canvas: canvas, context: view.context)
                closure(xcanvas)
            }
        }
        finally {
            if (canvas) {
                view.holder.unlockCanvasAndPost(canvas)
            }
        }
    }


}
