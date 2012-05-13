package org.blockout.world.event;

import java.util.Calendar;
import java.util.UUID;

public abstract class AbstractEvent<T extends IEvent<T>> implements IEvent<T> {

	protected Calendar	localTime;
	protected UUID		id;

	public AbstractEvent() {
		id = UUID.randomUUID();
		localTime = Calendar.getInstance();
	}

	@Override
	public UUID getId() {
		return id;
	}

	@Override
	public Calendar getLocalTime() {
		return localTime;
	}
}