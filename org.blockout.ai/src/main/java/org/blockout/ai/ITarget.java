package org.blockout.ai;

public interface ITarget {
	public int getPriority();

	public void approach();

	public boolean achieved();
}
