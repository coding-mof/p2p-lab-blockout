package org.blockout.ui;

import java.util.Properties;

import javax.inject.Inject;

import org.blockout.engine.SpriteType;
import org.blockout.world.LocalGameState;
import org.blockout.world.entity.Equipment;
import org.blockout.world.entity.Inventory;
import org.blockout.world.items.Armor;
import org.blockout.world.items.Elixir;
import org.blockout.world.items.Gloves;
import org.blockout.world.items.Helm;
import org.blockout.world.items.Item;
import org.blockout.world.items.Shield;
import org.blockout.world.items.Shoes;
import org.blockout.world.items.Weapon;

import de.lessvoid.nifty.EndNotify;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.builder.HoverEffectBuilder;
import de.lessvoid.nifty.controls.Controller;
import de.lessvoid.nifty.controls.Draggable;
import de.lessvoid.nifty.controls.Droppable;
import de.lessvoid.nifty.controls.DroppableDropFilter;
import de.lessvoid.nifty.controls.DroppableDroppedEvent;
import de.lessvoid.nifty.controls.dragndrop.builder.DraggableBuilder;
import de.lessvoid.nifty.controls.dragndrop.builder.DroppableBuilder;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.events.ElementShowEvent;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.xml.xpp3.Attributes;

/**
 * {@link Controller} for the inventory. Currently this class is not managed by
 * Spring since Nifty currently requires to create the instance on it's own.
 * 
 * @author Marc-Christian Schulze
 * 
 */
public class InventoryController implements Controller {

	protected Nifty				nifty;
	protected Screen			screen;
	protected LocalGameState	gameState;

	/**
	 * Dependency injection method used by Spring.
	 * 
	 * @param gameState
	 */
	@Inject
	public void setGameState( final LocalGameState gameState ) {
		this.gameState = gameState;
	}

	/**
	 * Gets invoked when the player drops an item on the inventory.
	 * 
	 * @param topic
	 * @param event
	 */
	@NiftyEventSubscriber(pattern = "inventory[0-9]+x[0-9]+")
	public void dropOnInventory( final String topic, final DroppableDroppedEvent event ) {
		System.out.println( "Inventory = ID: " + topic + ", Event: " + event + ", Source: " + event.getSource() );

		handleDropEvent( event );
	}

	/**
	 * Gets invoked when the player drops an item on the belt.
	 * 
	 * @param topic
	 * @param event
	 */
	@NiftyEventSubscriber(pattern = "belt[0-9]+")
	public void dropOnBelt( final String topic, final DroppableDroppedEvent event ) {
		System.out.println( "Belt = ID: " + topic + ", Event: " + event );
		handleDropEvent( event );
	}

	@NiftyEventSubscriber(id = "helm")
	public void dropOnHelm( final String topic, final DroppableDroppedEvent event ) {
		System.out.println( "Helm = ID: " + topic + ", Event: " + event );
		handleDropEvent( event );
	}

	@NiftyEventSubscriber(id = "armor")
	public void dropOnArmor( final String topic, final DroppableDroppedEvent event ) {
		System.out.println( "Armor = ID: " + topic + ", Event: " + event );
		handleDropEvent( event );
	}

	@NiftyEventSubscriber(id = "shoes")
	public void dropOnShoes( final String topic, final DroppableDroppedEvent event ) {
		System.out.println( "Shoes = ID: " + topic + ", Event: " + event );
		handleDropEvent( event );
	}

	@NiftyEventSubscriber(id = "weapon")
	public void dropOnWeapon( final String topic, final DroppableDroppedEvent event ) {
		System.out.println( "Weapon = ID: " + topic + ", Event: " + event );
		handleDropEvent( event );
	}

	@NiftyEventSubscriber(id = "shield")
	public void dropOnShield( final String topic, final DroppableDroppedEvent event ) {
		System.out.println( "Shield = ID: " + topic + ", Event: " + event );
		handleDropEvent( event );
	}

	@NiftyEventSubscriber(id = "gloves")
	public void dropOnGloves( final String topic, final DroppableDroppedEvent event ) {
		System.out.println( "Gloves = ID: " + topic + ", Event: " + event );
		handleDropEvent( event );
	}

	private void handleDropEvent( final DroppableDroppedEvent event ) {
		try {
			Item targetItem = getItem( event.getTarget() );
			if ( targetItem == null ) {
				Item item = removeItem( event.getSource() );
				setItem( event.getTarget(), item );
			}
		} catch ( Throwable e ) {
			e.printStackTrace();
			throw new Error( e );
		}
	}

	/**
	 * Gets invoked when the inventory shows up.
	 * 
	 * @param topic
	 * @param evt
	 */
	@NiftyEventSubscriber(id = "inventory_layer")
	public void showInventory( final String topic, final ElementShowEvent evt ) {
		try {
			if ( gameState != null ) {
				spawnEquipmentItems();
				spawnInventoryItems();

				setupEquipmentFilter();
			}
		} catch ( Throwable e ) {
			e.printStackTrace();
		}
	}

	private void spawnEquipmentItems() {
		Equipment equipment = gameState.getPlayer().getEquipment();

		spawnItem( "helm", "helmItem", equipment.getHelm() );
		spawnItem( "armor", "armorItem", equipment.getArmor() );
		spawnItem( "shoes", "shoesItem", equipment.getShoes() );
		spawnItem( "weapon", "weaponItem", equipment.getWeapon() );
		spawnItem( "shield", "shieldItem", equipment.getShield() );
		spawnItem( "gloves", "glovesItem", equipment.getGloves() );

		for ( int i = 0; i < 6; i++ ) {
			spawnItem( "belt" + i, "beltItem" + i, equipment.getBeltItem( i ) );
		}
	}

	private void spawnInventoryItems() {
		Inventory inventory = gameState.getPlayer().getInventory();
		for ( int x = 0; x < 5; x++ ) {
			for ( int y = 0; y < 6; y++ ) {

				String parentId = "inventory" + x + "x" + y;
				String newId = "item" + x + "x" + y;

				spawnItem( parentId, newId, inventory.getItem( x, y ) );
			}
		}
	}

	private void spawnItem( final String parentId, final String itemId, final Item item ) {
		Element element = screen.findElementByName( itemId );
		if ( element != null ) {
			element.markForRemoval();
		}
		if ( item != null ) {
			createDraggableItem( parentId, itemId, item.getSpriteType(), item.getDescription() );
		}
	}

	public void createDraggableItem( final String parentId, final String newId, final SpriteType spriteType,
			final String hintText ) {
		final DraggableBuilder builder = new DraggableBuilder() {
			{
				backgroundImage( spriteType.name() );
				id( newId );
				x( "0px" );
				y( "0px" );
				width( "32px" );
				height( "32px" );
				backgroundColor( "#00F3" );
				childLayoutCenter();
				onHoverEffect( new HoverEffectBuilder( "hint" ) {
					{
						effectParameter( "hintText", hintText );
					}
				} );
			}
		};
		final Droppable droppable = screen.findNiftyControl( parentId, Droppable.class );
		final DroppableBuilder dropBuilder = new DroppableBuilder() {
			{
				id( parentId );
				control( builder );
				x( droppable.getElement().getConstraintX().toString() );
				y( droppable.getElement().getConstraintY().toString() );
				width( "32px" );
				height( "32px" );
				childLayoutAbsolute();
			}
		};
		droppable.getElement().markForRemoval( new EndNotify() {

			@Override
			public void perform() {
				dropBuilder.build( nifty, screen, screen.findElementByName( "inventory" ) );
			}
		} );
	}

	@Override
	public void bind( final Nifty nifty, final Screen screen, final Element arg2, final Properties arg3,
			final Attributes arg4 ) {
		this.nifty = nifty;
		this.screen = screen;
	}

	@Override
	public void init( final Properties arg0, final Attributes arg1 ) {

	}

	private void setupEquipmentFilter() {
		setupInstanceFilter( "helm", Helm.class );
		setupInstanceFilter( "armor", Armor.class );
		setupInstanceFilter( "shoes", Shoes.class );
		setupInstanceFilter( "weapon", Weapon.class );
		setupInstanceFilter( "shield", Shield.class );
		setupInstanceFilter( "gloves", Gloves.class );
		setupBeltFilter();
	}

	private void setupInstanceFilter( final String id, final Class<?> clazz ) {
		Droppable droppable = screen.findNiftyControl( id, Droppable.class );
		droppable.removeAllFilters();
		droppable.addFilter( new DroppableDropFilter() {

			@Override
			public boolean accept( final Droppable source, final Draggable draggable, final Droppable target ) {
				Item item = getItem( source );
				return clazz.isInstance( item );
			}
		} );
	}

	private void setupBeltFilter() {
		for ( int x = 0; x < 6; x++ ) {
			Droppable droppable = screen.findNiftyControl( "belt" + x, Droppable.class );
			droppable.removeAllFilters();
			droppable.addFilter( new DroppableDropFilter() {

				@Override
				public boolean accept( final Droppable source, final Draggable draggable, final Droppable target ) {
					Item item = getItem( source );

					return item instanceof Elixir;
				}
			} );
		}
	}

	/**
	 * Returns the item on the droppable or null if no item is present. This
	 * method looks into the player's inventory and equipment to find the item.
	 * 
	 * @param droppable
	 * @return The item or null if not found.
	 */
	public Item getItem( final Droppable droppable ) {
		String id = droppable.getId();
		if ( id.startsWith( "belt" ) ) {
			int pos = Integer.parseInt( id.substring( 4 ) );
			return gameState.getPlayer().getEquipment().getBeltItem( pos );

		} else if ( id.startsWith( "inventory" ) ) {
			int posX = Integer.parseInt( id.substring( 9, 10 ) );
			int posY = Integer.parseInt( id.substring( 11, 12 ) );

			return gameState.getPlayer().getInventory().getItem( posX, posY );
		} else if ( id.equals( "helm" ) ) {
			return gameState.getPlayer().getEquipment().getHelm();
		} else if ( id.equals( "armor" ) ) {
			return gameState.getPlayer().getEquipment().getArmor();
		} else if ( id.equals( "shoes" ) ) {
			return gameState.getPlayer().getEquipment().getShoes();
		} else if ( id.equals( "weapon" ) ) {
			return gameState.getPlayer().getEquipment().getWeapon();
		} else if ( id.equals( "shield" ) ) {
			return gameState.getPlayer().getEquipment().getShield();
		} else if ( id.equals( "gloves" ) ) {
			return gameState.getPlayer().getEquipment().getGloves();
		}
		return null;
	}

	public Item removeItem( final Droppable droppable ) {
		Item item = null;
		String id = droppable.getId();
		if ( id.startsWith( "belt" ) ) {
			int pos = Integer.parseInt( id.substring( 4 ) );
			item = gameState.getPlayer().getEquipment().getBeltItem( pos );
			gameState.getPlayer().getEquipment().setBeltItem( pos, null );
		} else if ( id.startsWith( "inventory" ) ) {
			int posX = Integer.parseInt( id.substring( 9, 10 ) );
			int posY = Integer.parseInt( id.substring( 11, 12 ) );

			item = gameState.getPlayer().getInventory().removeItem( posX, posY );
		} else if ( id.equals( "helm" ) ) {
			item = gameState.getPlayer().getEquipment().getHelm();
			gameState.getPlayer().getEquipment().setHelm( null );
		} else if ( id.equals( "armor" ) ) {
			item = gameState.getPlayer().getEquipment().getArmor();
			gameState.getPlayer().getEquipment().setArmor( null );
		} else if ( id.equals( "shoes" ) ) {
			item = gameState.getPlayer().getEquipment().getShoes();
			gameState.getPlayer().getEquipment().setShoes( null );
		} else if ( id.equals( "weapon" ) ) {
			item = gameState.getPlayer().getEquipment().getWeapon();
			gameState.getPlayer().getEquipment().setWeapon( null );
		} else if ( id.equals( "shield" ) ) {
			item = gameState.getPlayer().getEquipment().getShield();
			gameState.getPlayer().getEquipment().setShield( null );
		} else if ( id.equals( "gloves" ) ) {
			item = gameState.getPlayer().getEquipment().getGloves();
			gameState.getPlayer().getEquipment().setGloves( null );
		}
		return item;
	}

	public void setItem( final Droppable droppable, final Item item ) {

		String id = droppable.getId();
		if ( id.startsWith( "belt" ) ) {
			int pos = Integer.parseInt( id.substring( 4 ) );
			gameState.getPlayer().getEquipment().setBeltItem( pos, (Elixir) item );
		} else if ( id.startsWith( "inventory" ) ) {
			int posX = Integer.parseInt( id.substring( 9, 10 ) );
			int posY = Integer.parseInt( id.substring( 11, 12 ) );

			gameState.getPlayer().getInventory().setItem( posX, posY, item );
		} else if ( id.equals( "helm" ) ) {
			gameState.getPlayer().getEquipment().setHelm( (Helm) item );
		} else if ( id.equals( "armor" ) ) {
			gameState.getPlayer().getEquipment().setArmor( (Armor) item );
		} else if ( id.equals( "shoes" ) ) {
			gameState.getPlayer().getEquipment().setShoes( (Shoes) item );
		} else if ( id.equals( "weapon" ) ) {
			gameState.getPlayer().getEquipment().setWeapon( (Weapon) item );
		} else if ( id.equals( "shield" ) ) {
			gameState.getPlayer().getEquipment().setShield( (Shield) item );
		} else if ( id.equals( "gloves" ) ) {
			gameState.getPlayer().getEquipment().setGloves( (Gloves) item );
		}
	}

	@Override
	public boolean inputEvent( final NiftyInputEvent evt ) {
		return true;
	}

	@Override
	public void onFocus( final boolean gotFocus ) {
	}

	@Override
	public void onStartScreen() {
	}
}
