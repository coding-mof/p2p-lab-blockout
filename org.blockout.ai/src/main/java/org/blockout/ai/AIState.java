package org.blockout.ai;

/**
 * Implementation of this class represent a state in the statemachine of an
 * artificial intelligence (AI)
 * 
 * @author Florian Müller
 * 
 */
public interface AIState {
    /**
     * Do the next step of this state
     * 
     * @param context
     *            Current {@link AIContext}
     */
    public void doNextStep( AIContext context );
}
