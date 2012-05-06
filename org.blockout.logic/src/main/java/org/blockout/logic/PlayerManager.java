package org.blockout.logic;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.inject.Named;

import org.blockout.common.IOUtils;
import org.blockout.world.entity.Player;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.map.ObjectMapper;

import com.google.common.base.Preconditions;

/**
 * This class is responsible for persisting player profiles. By default the
 * player profiles are stored in a subdirectory of the current working directory
 * named "profiles".
 * 
 * @author Marc-Christian Schulze
 * 
 */
@Named
public class PlayerManager {
	protected File	profileDirectory;

	/**
	 * Constructs a new player manager which stores the player profiles in a
	 * subdirectory of the current working directory named "profiles".
	 */
	public PlayerManager() {
		setProfileDirectory( new File( "profiles" ) );
	}

	/**
	 * Constructs a new player manager which stores the player profiles in the
	 * given directoy.
	 * 
	 * @param profileDirectory
	 *            The directoy in which to store the player profiles.
	 * @throws NullPointerException
	 *             If you pass in null.
	 * @throws IllegalArgumentException
	 *             If the given file doesn't denote a directory.
	 */
	public PlayerManager(final File profileDirectory) {
		setProfileDirectory( profileDirectory );
	}

	/**
	 * Sets the directory in which the player profiles get stored.
	 * 
	 * @param dir
	 *            The new directory in which to store the profiles.
	 * @throws NullPointerException
	 *             If you pass in null.
	 * @throws IllegalArgumentException
	 *             If the given file doesn't denote a directory.
	 */
	public void setProfileDirectory( final File dir ) {
		Preconditions.checkNotNull( dir );
		if ( !dir.exists() ) {
			dir.mkdirs();
		}
		if ( !dir.isDirectory() ) {
			throw new IllegalArgumentException( "Given path is not a directory: " + dir );
		}
		profileDirectory = dir;
	}

	/**
	 * Returns a set of player names or en empty one.
	 * 
	 * @return A set of player names or en empty one.
	 */
	public Set<String> listPlayers() {
		File[] files = profileDirectory.listFiles();
		Set<String> set = new HashSet<String>();
		if ( files == null ) {
			return set;
		}
		for ( File file : files ) {
			String filename = file.getName();
			if ( filename.endsWith( ".json" ) ) {
				set.add( filename.substring( 0, filename.length() - 5 ) );
			}
		}
		return set;
	}

	/**
	 * Opens the file of the player profile identified by the given playerName.
	 * 
	 * @param playerName
	 *            The name of the player.
	 * @return The reader for the profile file or null if the profile is not
	 *         present and creation flag was set to false.
	 * @throws IOException
	 *             If the profile was not present and couldn't get created.
	 */
	protected BufferedReader openProfileForReading( final String playerName, final boolean createFile )
			throws IOException {
		File profileFile = new File( profileDirectory, playerName + ".json" );
		try {
			return new BufferedReader( new FileReader( profileFile ) );
		} catch ( FileNotFoundException e ) {
			if ( !createFile ) {
				return null;
			}
			profileFile.createNewFile();
			return openProfileForReading( playerName, false );
		}
	}

	/**
	 * Opens the file of the player profile identified by the given playerName.
	 * 
	 * @param playerName
	 *            The name of the player.
	 * @return The reader for the profile file or null if the profile is not
	 *         present and creation flag was set to false.
	 * @throws IOException
	 *             If the profile was not present and couldn't get created.
	 */
	protected BufferedWriter openProfileForWriting( final String playerName, final boolean createFile )
			throws IOException {
		File profileFile = new File( profileDirectory, playerName + ".json" );
		try {
			return new BufferedWriter( new FileWriter( profileFile, false ) );
		} catch ( FileNotFoundException e ) {
			if ( !createFile ) {
				return null;
			}
			profileFile.createNewFile();
			return openProfileForWriting( playerName, false );
		}
	}

	private void configureFieldDetection( final ObjectMapper mapper ) {
		mapper.setVisibility( JsonMethod.FIELD, JsonAutoDetect.Visibility.ANY );
		mapper.setVisibility( JsonMethod.GETTER, JsonAutoDetect.Visibility.NONE );
		mapper.setVisibility( JsonMethod.IS_GETTER, JsonAutoDetect.Visibility.NONE );
	}

	/**
	 * Stores the given player profile. Any existing profile will be
	 * overwritten.
	 * 
	 * @param p
	 *            The player profile to persist.
	 * @throws IOException
	 *             If the profile couldn't get persisted due to an io error.
	 * @throws NullPointerException
	 *             If your pass in null.
	 */
	public void savePlayer( final Player p ) throws IOException {
		Preconditions.checkNotNull( p );
		ObjectMapper mapper = new ObjectMapper();
		configureFieldDetection( mapper );

		BufferedWriter writer = null;
		try {
			writer = openProfileForWriting( p.getName(), true );
			mapper.writeValue( writer, p );
		} finally {
			IOUtils.close( writer );
		}
	}

	/**
	 * Loads the profile of the player named <code>playerName</code>
	 * 
	 * @param playerName
	 *            The name of the player profile to load.
	 * @return The player profile or null if it doesn't exist.
	 * @throws IOException
	 *             If an io error occurred during reading the profile.
	 * @throws NullPointerException
	 *             If you pass in null.
	 */
	public Player loadPlayer( final String playerName ) throws IOException {
		Preconditions.checkNotNull( playerName );
		ObjectMapper mapper = new ObjectMapper();
		configureFieldDetection( mapper );

		BufferedReader reader = null;
		try {
			reader = openProfileForReading( playerName, false );
			if ( reader == null ) {
				return null;
			}
			return mapper.readValue( reader, Player.class );
		} finally {
			IOUtils.close( reader );
		}
	}

	/**
	 * Permanently deletes the player profile identified by the given name.
	 * 
	 * @param playerName
	 *            The name of the profile to delete.
	 * @throws NullPointerException
	 *             If you pass in null.
	 */
	public void deletePlayer( final String playerName ) {
		Preconditions.checkNotNull( playerName );
		File profileFile = new File( profileDirectory, playerName + ".json" );
		if ( profileFile.exists() ) {
			profileFile.delete();
		}
	}

	/**
	 * Permanently deletes all player profiles.
	 */
	public void deleteAllPlayers() {
		Set<String> playerNames = listPlayers();
		for ( String playerName : playerNames ) {
			deletePlayer( playerName );
		}
	}
}
