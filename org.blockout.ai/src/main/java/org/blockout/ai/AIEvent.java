package org.blockout.ai;

import org.blockout.common.TileCoordinate;

/**
 * Class to represent event in which a AI is interested with the position where
 * the event is happend a optional extra data.
 * 
 * @author Florian Müller
 * 
 * @param <E>
 *            Type of the data which is provided with this event
 */
public class AIEvent<E> {
    private final AIEventType    type;
    private final TileCoordinate position;
    private final E              data;

    /**
     * Default Constructor that creates an unknown event
     */
    public AIEvent() {
        this.type = AIEventType.UNKNOWN;
        this.position = null;
        this.data = null;
    }

    /**
     * Constructor to create an event with no position and no extra data
     * 
     * @param type
     *            Type of the event
     */
    public AIEvent( final AIEventType type ) {
        this.type = type;
        this.position = null;
        this.data = null;
    }

    /**
     * Constructor to create an event on a specific position
     * 
     * @param type
     *            Type of the event
     * @param position
     *            Position where the event occurred
     */
    public AIEvent( final AIEventType type, final TileCoordinate position ) {
        this.type = type;
        this.position = position;
        this.data = null;
    }

    /**
     * Constructor to create an event with some extra data
     * 
     * @param type
     *            Type of the event
     * @param data
     *            Extra data for this event
     */
    public AIEvent( final AIEventType type, final E data ) {
        this.type = type;
        this.position = null;
        this.data = data;
    }

    /**
     * Constructor to create an event on a specific position and with some extra
     * data
     * 
     * @param type
     *            Type of the event
     * @param position
     *            Position where the event occurred
     * @param data
     *            Extra data for this event
     */
    public AIEvent( final AIEventType type, final TileCoordinate position,
            final E data ) {
        this.type = type;
        this.position = position;
        this.data = data;
    }

    /**
     * Returns the type of this event
     * 
     * @return Type of this event
     */
    public AIEventType getType() {
        return type;
    }

    /**
     * Returns the position where this event occurred
     * 
     * @return Position of this event
     */
    public TileCoordinate getPosition() {
        return position;
    }

    /**
     * Returns if the event has a position
     * 
     * @return True of there is a valid position, false otherwise
     */
    public boolean hasPosition() {
        return null != position;
    }

    /**
     * Returns the provided data of this event
     * 
     * @return Data of this event
     */
    public E getData() {
        return data;
    }

    /**
     * Returns if this event has some data
     * 
     * @return True if the is some extra data, false otherwise
     */
    public boolean hasData() {
        return null != data;
    }
}
