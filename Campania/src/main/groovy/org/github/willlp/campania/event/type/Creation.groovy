package org.github.willlp.campania.event.type

import org.github.willlp.campania.event.EventType

/**
 * Created by will on 28/12/14.
 */
enum Creation implements EventType {
    ENEMY_CREATED,
    ENEMY_DESTROYED,

    ITEM_CREATED,

    SHOOT_CREATED,
    SHOOT_DESTROYED,

    SCENARIO_ELEMENT_DESTROYED,

    TIMED_EVENT_CREATED,
    TIMED_EVENT_DESTROYED
}
