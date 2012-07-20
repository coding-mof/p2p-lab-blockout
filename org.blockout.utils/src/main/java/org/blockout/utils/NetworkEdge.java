package org.blockout.utils;

public class NetworkEdge {

    private static long next_index = 0;

    private long        index;
    private String      type;
    private String      label;

    public NetworkEdge( final String type, final String label ) {
        this.type = type;
        this.label = label;
        this.index = next_index++;
    }

    public String getType() {
        return type;
    }

    public String getLabel() {
        return label;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) ( index ^ ( index >>> 32 ) );
        return result;
    }

    @Override
    public boolean equals( Object obj ) {
        if( this == obj )
            return true;

        if( obj == null )
            return false;

        if( getClass() != obj.getClass() )
            return false;

        NetworkEdge other = (NetworkEdge) obj;

        if( index != other.index )
            return false;
        return true;
    }

    @Override
    public String toString() {
        return label;
    }
}
