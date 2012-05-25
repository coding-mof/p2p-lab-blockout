package org.blockout.world.messeges;

import org.blockout.network.message.IMessage;
import org.blockout.world.event.IEvent;
import org.blockout.world.state.IStateMachine;

/**
 * Message used to deliver IEvents from the {@link IStateMachine} 
 * 
 * @author key3
 *
 */
public class StateMessage implements IMessage{

	private static final long serialVersionUID = 4413756278358237464L;
	
	public static final int COMMIT_MESSAGE	 = 1;
	public static final int Push_MESSAGE	 = 2;
	public static final int ROLLBAK_MESSAGE	 = 3;
	
	
	private IEvent<?> event;
	private int type;
	
	
	
	public StateMessage(IEvent<?> event, int type) {
		super();
		this.event = event;
		this.type = type;
	}

	public IEvent<?> getEvent() {
		return event;
	}

	public int getType() {
		return type;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		StateMessage other = (StateMessage) obj;
		if (event == null) {
			if (other.event != null)
				return false;
		} else if (!event.equals(other.event))
			return false;
		if (type != other.type)
			return false;
		return true;
	}
}
