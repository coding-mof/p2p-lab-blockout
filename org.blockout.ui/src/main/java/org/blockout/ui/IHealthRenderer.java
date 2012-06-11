package org.blockout.ui;

public interface IHealthRenderer {

	public abstract void init();

	public abstract void update( final long deltaMillis );

	public abstract void render();

}