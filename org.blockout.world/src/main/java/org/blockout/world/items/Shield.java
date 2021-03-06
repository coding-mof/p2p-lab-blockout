package org.blockout.world.items;

import org.blockout.engine.SpriteType;

import com.google.common.base.Preconditions;

/**
 * Instances of this class represent shields that can be carried by the player.
 * 
 * @author Marc-Christian Schulze
 * 
 */
public class Shield implements Item {

    private static final long serialVersionUID = 3664422684819188243L;
    protected int             protection;

    public Shield( final int protection ) {
        Preconditions.checkArgument( protection >= 0,
                "Protection must be greater or equal to zero." );
        this.protection = protection;
    }

    public int getProtection() {
        return protection;
    }

    @Override
    public String getName() {
        return "Shield";
    }

    @Override
    public String toString() {
        return "Shield[protection=" + protection + "]";
    }

    @Override
    public SpriteType getSpriteType() {
        return SpriteType.Shield;
    }

    @Override
    public String getDescription() {
        return "Shield\nProtection: " + getProtection();
    }
}
