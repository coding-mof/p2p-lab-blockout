package org.blockout.world.messeges;

import org.blockout.network.message.IMessage;
import org.blockout.world.entity.Player;

public class EnterGameMessage implements IMessage {
	
	private static final long serialVersionUID = -8748709948028506863L;
	
	private Player player;

	public EnterGameMessage(Player player) {
		this.player = player;
	}

	public Player getPlayer() {
		return player;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EnterGameMessage other = (EnterGameMessage) obj;
		if (player == null) {
			if (other.player != null)
				return false;
		} else if (!player.equals(other.player))
			return false;
		return true;
	}
	
	
}
