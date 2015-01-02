package org.github.willlp.campania.ui

import android.content.Context
import android.util.Log
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import groovy.transform.CompileStatic
import org.github.willlp.campania.Loop
import org.github.willlp.campania.event.EventManager

/**
 * Created by will on 27/12/14.
 */
@CompileStatic
class GameView extends SurfaceView implements SurfaceHolder.Callback {

    static String TAG = GameView.simpleName

    /*
    Following instructions on
    http://www.javacodegeeks.com/2011/07/android-game-development-basic-game_05.html
     */

    EventManager eventManager = EventManager.instance

    GameView(Context context) {
        super(context)
        Log.d(TAG, 'Created GameView')
        holder.addCallback this
        setFocusable true

        new Loop(view: this).start()
    }


    @Override
    boolean onTouchEvent(MotionEvent event) {
        Log.d TAG, event.toString()
    }


    @Override
    void surfaceCreated(SurfaceHolder surfaceHolder) {
        Log.d TAG, 'surfaceCreated'
    }

    @Override
    void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        Log.d TAG, 'surfaceChanged'
    }

    @Override
    void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        Log.d TAG, 'surfaceDestroyed'
    }


    def draw (XCanvas canvas) {

        /*
        Blockinger picks around 25% for controls at each side of the screen
        and around 50% to scenario canvas at the middle.
        Let's use the portrait width as a limited landscape scenario width.
         */

        drawScenario(canvas)

        if (canvas.landscape) {
            drawLateralControls(canvas)
        } else {
            drawBottomControls(canvas)
        }

    }


    def drawScenario(XCanvas canvas) {
        def size = canvas.screenSize
        canvas.drawRect Dimension.scenario(canvas), canvas.color.cyan
    }


    def drawLateralControls(XCanvas canvas) {
        def size = canvas.screenSize
        def controlWidth = Dimension.getControlWidth(canvas)

        canvas.drawRect(0, 0, controlWidth, size.y, canvas.color.black)
        canvas.drawRect(size.x - controlWidth, 0, size.x, size.y, canvas.color.black)
    }


    def drawBottomControls(XCanvas canvas) {
        def size = canvas.screenSize
        canvas.drawRect(0, size.x, size.x, size.y, canvas.color.black)
    }


}
