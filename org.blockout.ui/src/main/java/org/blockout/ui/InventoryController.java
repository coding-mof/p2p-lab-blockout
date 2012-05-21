package org.blockout.ui;

import java.util.Properties;

import javax.inject.Named;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.controls.Controller;
import de.lessvoid.nifty.controls.DroppableDroppedEvent;
import de.lessvoid.nifty.controls.dragndrop.builder.DraggableBuilder;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.events.NiftyMousePrimaryClickedEvent;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import de.lessvoid.xml.xpp3.Attributes;

/**
 * {@link ScreenController} for the inventory.
 * 
 * @author Marc-Christian Schulze
 * 
 */
@Named
public class InventoryController implements Controller {

	protected Nifty		nifty;
	protected Screen	screen;

	@NiftyEventSubscriber(pattern = "inventory[0-9]+x[0-9]+")
	public void dropOnInventory( final String topic, final DroppableDroppedEvent event ) {
		System.out.println( "Inventory = ID: " + topic + ", Event: " + event );
	}

	@NiftyEventSubscriber(pattern = "belt[0-9]+")
	public void dropOnBelt( final String topic, final DroppableDroppedEvent event ) {
		System.out.println( "Belt = ID: " + topic + ", Event: " + event );
	}

	@NiftyEventSubscriber(id = "inventory")
	public void discard( final String topic, final NiftyMousePrimaryClickedEvent evt ) {
		System.out.println( "Discarding " + evt );

	}

	public void spawnDraggable() {
		DraggableBuilder builder = new DraggableBuilder() {
			{
				x( "0px" );
				y( "0px" );
				width( "32px" );
				height( "32px" );
				backgroundColor( "#F00A" );
				childLayoutCenter();
			}
		};

		Element drop4x1 = screen.findElementByName( "inventory4x1" );
		builder.build( nifty, screen, drop4x1 );
	}

	public void discard() {
	}

	@Override
	public void bind( final Nifty nifty, final Screen screen, final Element arg2, final Properties arg3,
			final Attributes arg4 ) {
		this.nifty = nifty;
		this.screen = screen;
		spawnDraggable();
	}

	@Override
	public void init( final Properties arg0, final Attributes arg1 ) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean inputEvent( final NiftyInputEvent evt ) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void onFocus( final boolean gotFocus ) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStartScreen() {
		// TODO Auto-generated method stub

	}

}
