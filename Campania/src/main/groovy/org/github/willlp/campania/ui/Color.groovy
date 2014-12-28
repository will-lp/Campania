package org.github.willlp.campania.ui

import android.graphics.Color as AColor
import android.graphics.Paint
import groovy.transform.PackageScope

/**
 * Created by will on 28/12/14.
 */
@PackageScope
class Color {
    private Paint getColor(int color) { new Paint(color: color) }
    Paint getBlack() { getColor AColor.BLACK }
    Paint getCyan() { getColor AColor.CYAN }
}
