package org.github.willlp.campania.ui

import android.content.Context
import android.util.Log
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import groovy.transform.CompileStatic
import groovy.transform.stc.ClosureParams
import groovy.transform.stc.SimpleType
import org.github.willlp.campania.ElementContainer
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

    ElementContainer container
    Loop loop

    Map<String, Dimension> controls = [:]

    GameView(Context context) {
        super(context)
        Log.d(TAG, 'Created GameView')

        holder.addCallback this
        setFocusable true
    }


    @Override
    boolean onTouchEvent(MotionEvent event) {
        Log.d TAG, event.toString()
    }


    @Override
    void surfaceCreated(SurfaceHolder surfaceHolder) {
        Log.d TAG, 'surfaceCreated'

        withCanvas(surfaceHolder) { canvas ->
            container = ElementContainer.start canvas
            loop = new Loop(view: this, container: container).start()
            if (canvas.landscape) {
                loadLandscapeControlDimensions(canvas)
            }
            else {
                loadPortraitControlDimensions(canvas)
            }
        }
    }


    @Override
    void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        withCanvas(surfaceHolder) { canvas ->
            if (canvas.landscape) {
                convertElementsPositionToLandscape(canvas)
                loadLandscapeControlDimensions(canvas)
            }
            else {
                convertElementsPositionToPortrait(canvas)
                loadPortraitControlDimensions(canvas)
            }
        }
        Log.d TAG, 'surfaceChanged'
    }


    @Override
    void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        Log.d TAG, 'surfaceDestroyed'
    }


    def withCanvas(@ClosureParams(value=SimpleType, options="org.github.willlp.campania.ui.XCanvas") Closure closure) {
        withCanvas(holder, closure)
    }


    def withCanvas(
            SurfaceHolder holder,
            @ClosureParams(value=SimpleType, options="org.github.willlp.campania.ui.XCanvas") Closure closure) {
        def canvas
        try {
            canvas = holder.lockCanvas()
            if (canvas) {
                def xcanvas = new XCanvas(canvas: canvas, context: context)
                closure(xcanvas)
            }
        }
        catch(e) {
            Log.e TAG, "Error on withCanvas", e
        }
        finally {
            if (canvas) {
                holder.unlockCanvasAndPost(canvas)
            }
        }
    }


    def convertElementsPositionToLandscape(XCanvas canvas) {
        def controlWidth = new Dimension(canvas).landscapeControlWidth
        container.allElements.each { it.x += controlWidth }
    }


    def convertElementsPositionToPortrait(XCanvas canvas) {
        def controlWidth = new Dimension(canvas).landscapeControlWidth
        container.allElements.each { it.x -= controlWidth }
    }


    def drawScenarioAndControls(XCanvas canvas) {

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
        canvas.drawRect new Dimension(canvas).scenario, canvas.color.cyan
    }


    def drawLateralControls(XCanvas canvas) {
        canvas.drawRect(controls.leftPanel,  canvas.color.black)
        canvas.drawRect(controls.rightPanel, canvas.color.black)
        canvas.drawRect(controls.left,       canvas.color.white)
        canvas.drawRect(controls.right,      canvas.color.white)
        canvas.drawRect(controls.pause,      canvas.color.white)
        canvas.drawRect(controls.shoot,      canvas.color.red)
    }


    def drawBottomControls(XCanvas canvas) {
        def size = canvas.screenSize
        canvas.drawRect(controls.panel, canvas.color.black)
        canvas.drawRect(controls.left,  canvas.color.white)
        canvas.drawRect(controls.right, canvas.color.white)
        canvas.drawRect(controls.pause, canvas.color.white)
        canvas.drawRect(controls.shoot, canvas.color.red)
    }


    def loadLandscapeControlDimensions(XCanvas canvas) {
        def size = canvas.screenSize
        def dimension = new Dimension(canvas)
        def controlWidth = dimension.controlPanelWidth
        def scenario = dimension.scenario
        def buttonWidth = (int) (controlWidth - 30) / 2
        def buttonHeight = (int) (size.y + 20) / 2

        controls.leftPanel = new Dimension(
                left   : 0,
                top    : 0,
                right  : controlWidth,
                bottom : size.y)

        controls.rightPanel = new Dimension(
                left   : size.x - controlWidth,
                top    : 0,
                right  : size.x,
                bottom : size.y)

        controls.left = new Dimension(
                left   : 10,
                top    : 10,
                right  : buttonWidth + 10,
                bottom : buttonHeight)

        controls.right = new Dimension(
                left   : buttonWidth + 20,
                top    : 10,
                right  : buttonWidth * 2 + 20,
                bottom : buttonHeight)

        controls.pause = new Dimension(
                left   : 10,
                top    : buttonHeight  + 20,
                right  : buttonWidth * 2 + 20,
                bottom : (int) buttonHeight + buttonHeight / 3)

        controls.shoot = new Dimension(
                left   : scenario.right + 10,
                top    : 10,
                right  : scenario.right + buttonWidth * 2 + 20,
                bottom : buttonHeight)
    }


    def loadPortraitControlDimensions(XCanvas canvas) {
        def size = canvas.screenSize
        def dimension = new Dimension(canvas)
        def controlWidth = dimension.controlPanelWidth / 2
        def scenario = dimension.scenario
        def buttonWidth = (int) (controlWidth - 30) / 2
        def buttonHeight = (int) (size.y - 20) / 2

        println "scenario=$scenario"

        controls.panel = new Dimension(
                left   : 0,
                top    : scenario.bottom,
                right  : scenario.bottom,
                bottom : size.y)

        controls.left = new Dimension(
                left   : 10,
                top    : scenario.bottom + 10,
                right  : buttonWidth + 10,
                bottom : scenario.bottom + 10 + buttonHeight)

        controls.right = new Dimension(
                left   : buttonWidth + 20,
                top    : scenario.bottom + 10,
                right  : buttonWidth * 2 + 20,
                bottom : scenario.bottom + 10 + buttonHeight)

        controls.pause = new Dimension(
                left   : 10,
                top    : scenario.bottom + 20 + buttonHeight,
                right  : buttonWidth * 2 + 20,
                bottom : scenario.bottom + 20 + buttonHeight + 30)

        controls.shoot = new Dimension(
                left   : size.x / 2 + 10,
                top    : scenario.bottom + 10,
                right  : size.x - 10,
                bottom : scenario.bottom + 10 + buttonHeight)
    }


}
