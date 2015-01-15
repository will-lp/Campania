package org.github.willlp.campania.ui

import android.content.Context
import android.content.res.Configuration
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Point
import android.view.WindowManager
import groovy.transform.CompileStatic

/**
 * Created by will on 27/12/14.
 */
@CompileStatic
class XCanvas {

    @Delegate Canvas canvas
    Context context
    private Point size
    Color color = new Color()


    Point getScreenSize() {
        if (!size) { // let's cache it, it won't change so soon ;-)
            size = new Point()
            windowManager.defaultDisplay.getSize(size)
        }
        size
    }


    WindowManager getWindowManager() {
        (WindowManager) context.getSystemService(Context.WINDOW_SERVICE)
    }


    Boolean getLandscape() {
        def rotation = windowManager.defaultDisplay.rotation
        context.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    }


    void drawRect(Dimension d, Paint paint) {
        canvas.drawRect(d.left, d.top, d.right, d.bottom, paint)
    }

}
