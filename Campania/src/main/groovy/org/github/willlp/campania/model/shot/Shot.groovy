package org.github.willlp.campania.model.shot

import org.github.willlp.campania.model.Element

/**
 * Created by will on 27/12/14.
 */
abstract class Shot extends Element {
    int damage = 1
    ShotType type
}
