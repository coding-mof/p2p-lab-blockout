package org.blockout.world.items;

import org.blockout.engine.SpriteType;

import com.google.common.base.Preconditions;

/**
 * Instances of this clas respresent a armor that a player can wear.
 * 
 * @author Marc-Christian Schulze
 * 
 */
public class Armor implements Item {

    private static final long serialVersionUID = -3696812770197615582L;
    protected int             protection;

    public Armor( final int protection ) {
        Preconditions.checkArgument( protection >= 0,
                "Protection must be greater or equal to zero." );
        this.protection = protection;
    }

    public int getProtection() {
        return protection;
    }

    @Override
    public String getName() {
        return "Armor";
    }

    @Override
    public String toString() {
        return "Armor[protection=" + protection + "]";
    }

    @Override
    public SpriteType getSpriteType() {
        return SpriteType.Armor;
    }

    @Override
    public String getDescription() {
        return "Armor\nProtection: " + getProtection();
    }
}
