package org.blockout.engine;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * Unit-Test for the SpriteManagerImplementation.
 * 
 * @author Marc-Christian Schulze
 * 
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(Utils.class)
public class TestSpriteManagerImpl {

	protected SpriteMapping		mapping;
	protected ISpriteSheet		spriteSheet;
	protected SpriteManagerImpl	spriteManager;

	@Before
	public void setUp() {
		mapping = new SpriteMapping();
		spriteSheet = Mockito.mock( ISpriteSheet.class );
		spriteManager = new SpriteManagerImpl( spriteSheet );
	}

	/**
	 * Tests if caching for normal (images without excluded background) are
	 * cached correctly.
	 */
	@Test
	public void testSpriteCache() {
		Image spriteMock = Mockito.mock( Image.class );
		Mockito.doReturn( spriteMock ).when( spriteSheet ).getSprite( mapping.getSpriteId( SpriteType.Player ) );

		Image sprite = spriteManager.getSprite( SpriteType.Player );
		Image cachedSprite = spriteManager.getSprite( SpriteType.Player );

		assertTrue( sprite == cachedSprite );
	}

	/**
	 * Tests if caching for images with excluded background is working.
	 * 
	 * @throws SlickException
	 */
	@Test
	public void testExcludedSpriteCache() throws SlickException {

		// internally used by the SpriteManager
		PowerMockito.mockStatic( Utils.class );

		Image mock1 = Mockito.mock( Image.class );
		Image mock2 = Mockito.mock( Image.class );
		Image spriteMock = Mockito.mock( Image.class );
		Image bgMock = Mockito.mock( Image.class );

		Mockito.doReturn( spriteMock ).when( spriteSheet ).getSprite( mapping.getSpriteId( SpriteType.Player ) );
		Mockito.doReturn( bgMock ).when( spriteSheet ).getSprite( mapping.getSpriteId( SpriteType.stoneground ) );

		PowerMockito.doReturn( mock1 ).when( Utils.class );
		Utils.exclude( bgMock, spriteMock );
		Image sprite = spriteManager.getSprite( SpriteType.Player, true );

		PowerMockito.doReturn( mock2 ).when( Utils.class );
		Utils.exclude( bgMock, spriteMock );
		Image cachedSprite = spriteManager.getSprite( SpriteType.Player, true );

		assertNotNull( sprite );
		assertNotNull( cachedSprite );
		assertTrue( sprite == cachedSprite );
	}
}
