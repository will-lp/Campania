package org.github.willlp.campania.ui

import android.content.Context
import android.graphics.Rect
import android.util.Log
import android.view.*
import groovy.transform.CompileStatic
import groovy.transform.stc.ClosureParams
import groovy.transform.stc.SimpleType
import org.github.willlp.campania.ElementContainer
import org.github.willlp.campania.Loop
import org.github.willlp.campania.event.Event
import org.github.willlp.campania.event.EventManager
import org.github.willlp.campania.event.EventType
import org.github.willlp.campania.event.type.Game
import org.github.willlp.campania.event.type.Move

/**
 * Created by will on 27/12/14.
 */
@CompileStatic
class GameView extends SurfaceView implements SurfaceHolder.Callback, View.OnKeyListener {

    static String TAG = GameView.simpleName

    /*
    Following instructions on
    http://www.javacodegeeks.com/2011/07/android-game-development-basic-game_05.html
     */

    EventManager eventManager = EventManager.instance
    ElementContainer container
    Loop loop
    Map<String, Dimension> controls = [:]
    boolean started = false // because the surface changes on the beginning

    GameView(Context context) {
        super(context)
        Log.d(TAG, 'Created GameView')

        holder.addCallback this
        setFocusable true

        setOnKeyListener(this)
    }

    @Override
    boolean onKey(View view, int keycode, KeyEvent event) {
        EventType type

        def buttonUp = (event.action == KeyEvent.ACTION_UP)

        if (event.keyCode in [KeyEvent.KEYCODE_DPAD_LEFT, KeyEvent.KEYCODE_2]) {
            type = buttonUp ? Move.STOP : Move.LEFT
        }
        else if (event.keyCode in [KeyEvent.KEYCODE_DPAD_RIGHT, KeyEvent.KEYCODE_3]) {
            type = buttonUp ? Move.STOP : Move.RIGHT
        }
        else if (buttonUp) {
            if (event.keyCode in [KeyEvent.KEYCODE_SPACE, KeyEvent.KEYCODE_7]) {
                type = Move.HERO_SHOOT
            }
            else if (event.keyCode in [KeyEvent.KEYCODE_P, KeyEvent.KEYCODE_4]) {
                type = Game.PAUSE
            }
        }

        eventManager.raise new Event(type: type)

        true
    }

    @Override
    boolean onTouchEvent(MotionEvent event) {
        EventType eventType

        Log.d TAG, event.toString()

        def buttonUp = (event.action == MotionEvent.ACTION_UP || event.action == MotionEvent.ACTION_POINTER_UP)

        if (controls.left.contains(event.x, event.y)) {
            eventType = buttonUp ? Move.STOP : Move.LEFT
        }
        else if (controls.right.contains(event.x, event.y)) {
            eventType = buttonUp ? Move.STOP : Move.RIGHT
        }
        else if (buttonUp) {
            if (controls.shoot.contains(event.x, event.y)) {
                eventType = Move.HERO_SHOOT
            } else if (controls.pause.contains(event.x, event.y)) {
                eventType = Game.PAUSE
            }
        }

        eventManager.raise(new Event(type: eventType))
    }


    @Override
    void surfaceCreated(SurfaceHolder surfaceHolder) {
        Log.d TAG, 'surfaceCreated'
    }


    def startElements(XCanvas canvas) {
        container = ElementContainer.start canvas
        loop = new Loop(view: this, container: container).start()
        if (canvas.landscape) {
            loadLandscapeControlDimensions(canvas)
        } else {
            loadPortraitControlDimensions(canvas)
        }
        started = true
    }


    @Override
    void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        Log.d TAG, 'surfaceChanged'

        withCanvas(surfaceHolder) { canvas ->
            if (!started) {
                startElements canvas
            }
            else {
                if (canvas.landscape) {
                    convertElementsPositionToLandscape(canvas)
                    loadLandscapeControlDimensions(canvas)
                } else {
                    convertElementsPositionToPortrait(canvas)
                    loadPortraitControlDimensions(canvas)
                }
                container.hero.y = (int) new Dimension(canvas).scenario.bottom - container.hero.height
            }
        }
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
        drawInfoAndCommonControls(canvas)
    }


    def drawBottomControls(XCanvas canvas) {
        canvas.drawRect(controls.panel, canvas.color.black)
        drawInfoAndCommonControls(canvas)
    }


    def drawInfoAndCommonControls(XCanvas canvas) {
        canvas.drawRect(controls.left,  canvas.color.white)
        canvas.drawRect(controls.right, canvas.color.white)
        canvas.drawRect(controls.pause, canvas.color.white)
        canvas.drawRect(controls.shoot, canvas.color.red)


        def closures = {
            def weakMap = eventManager.subscribers.values()
            "objects: ${weakMap*.keySet().flatten().size()}"
        }()

        def texts = [
                "Max shots: $container.hero.maxShots",
                "Speed: $container.hero.speed",
                "Score: $container.gameStatus.score",
                "Lives: $container.hero.lives",
                "x: $canvas.screenSize.x, y: $canvas.screenSize.y",
                "allElements size: ${container.allElements.size()}",
                "eventsMap.size: ${eventManager.subscribers.size()}, ${closures}"]


        def yIndex = controls.info.top
        def white = canvas.color.white
        white.setTextSize(20f)
        def rect = new Rect()
        white.getTextBounds(texts.head().toString(), 0, 1, rect)

        for (text in texts) {
            canvas.drawText( text, controls.info.left, yIndex, canvas.color.white )
            yIndex += rect.height()
        }
    }


    def loadLandscapeControlDimensions(XCanvas canvas) {
        def size = canvas.screenSize
        def dimension = new Dimension(canvas)
        def controlWidth = dimension.controlPanelWidth
        def scenario = dimension.scenario
        def buttonWidth = (int) (controlWidth - 30) / 2
        def buttonHeight = (int) size.y / 2

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
                top    : size.y - buttonHeight,
                right  : buttonWidth + 10,
                bottom : size.y - 10)

        controls.right = new Dimension(
                left   : buttonWidth + 20,
                top    : size.y - buttonHeight,
                right  : buttonWidth * 2 + 20,
                bottom : size.y - 10)

        controls.shoot = new Dimension(
                left   : scenario.right + 10,
                top    : size.y - buttonHeight,
                right  : size.x - 10,
                bottom : size.y - 10)

        controls.pause = new Dimension(
                left   : size.x - buttonWidth - 10,
                top    : controls.shoot.top - 80,
                right  : controls.shoot.right,
                bottom : controls.shoot.top - 10)

        controls.info = new Dimension(
                left   : 10,
                top    : 10,
                right  : -1, // not used
                bottom : -1) // not used
    }


    def loadPortraitControlDimensions(XCanvas canvas) {
        def size = canvas.screenSize
        def dimension = new Dimension(canvas)
        def controlWidth = dimension.controlPanelWidth / 2
        def scenario = dimension.scenario
        def buttonWidth = (int) (controlWidth - 30) / 2
        def buttonHeight = (int) (size.y - 20) / 2

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

        controls.info = new Dimension(
                left   : -1,
                top    : -1,
                right  : -1,
                bottom : -1)
    }


}
