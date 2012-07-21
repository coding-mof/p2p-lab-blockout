package org.blockout.world.items;

import org.blockout.engine.SpriteType;

import com.google.common.base.Preconditions;

/**
 * Instances of this class represent helms that the player can wear to increase
 * it's protection.
 * 
 * @author Marc-Christian Schulze
 * 
 */
public class Helm implements Item {

    private static final long serialVersionUID = -3350831942032949111L;
    protected int             protection;

    public Helm( final int protection ) {
        Preconditions.checkArgument( protection >= 0,
                "Protection must be greater or equal to zero." );
        this.protection = protection;
    }

    public int getProtection() {
        return protection;
    }

    @Override
    public String getName() {
        return "Helm";
    }

    @Override
    public String toString() {
        return "Helm[protection=" + protection + "]";
    }

    @Override
    public SpriteType getSpriteType() {
        return SpriteType.Helm;
    }

    @Override
    public String getDescription() {
        return "Helm\nProtection: " + getProtection();
    }

}
