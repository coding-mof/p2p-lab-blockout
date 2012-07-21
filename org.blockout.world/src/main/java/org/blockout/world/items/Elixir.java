package org.blockout.world.items;

import org.blockout.engine.SpriteType;

import com.google.common.base.Preconditions;

/**
 * Implementation of exlixir that can be carried in the player's belt.
 * 
 * @author Marc-Christian Schulze
 * 
 */
public class Elixir implements Item {

    private static final long serialVersionUID = 193471324522183511L;
    protected Type            type;
    protected int             points;

    public Elixir( final Type type, final int points ) {
        Preconditions.checkNotNull( type );
        Preconditions.checkArgument( points > 0,
                "Points must be greater than zero." );
        this.type = type;
        this.points = points;
    }

    public Type getType() {
        return type;
    }

    public int getPoints() {
        return points;
    }

    public static enum Type {
        Health
    }

    @Override
    public String getName() {
        switch ( type ) {
        case Health:
            return "Health Elixir";
        default:
            return "Unknown Elixir";
        }
    }

    @Override
    public String toString() {
        return "Elixir[type=" + type + ", points=" + points + "]";
    }

    @Override
    public SpriteType getSpriteType() {
        return SpriteType.elexir_red;
    }

    @Override
    public String getDescription() {
        return "Elixir\nHealthPoints: "
                + getPoints()
                + "\n\nPut into belt and press <Space>\nto heal your character.";
    }
}
