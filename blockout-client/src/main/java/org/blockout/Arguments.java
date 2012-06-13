package org.blockout;

import com.beust.jcommander.Parameter;

/**
 * Container class for all command line arguments parsed by JCommander.
 * 
 * @author Marc-Christian Schulze
 * 
 */
public class Arguments {
	@Parameter(names = { "-h", "--help" }, description = "Prints the help to stdout")
	private boolean					showHelp;

	@Parameter(names = { "-l", "--loopback" }, description = "Run all network traffic through the loopback interface")
	private boolean					loopback;

	private boolean					headless;
	private final HeadlessCommand	headlessCmd		= new HeadlessCommand();

	private boolean					spectator;
	private final SpectatorCommand	spectatorCmd	= new SpectatorCommand();

	public boolean isSpectator() {
		return spectator;
	}

	public void setSpectator( final boolean spectator ) {
		this.spectator = spectator;
	}

	public SpectatorCommand getSpectatorCmd() {
		return spectatorCmd;
	}

	public boolean isHeadless() {
		return headless;
	}

	public boolean isLoopback() {
		return loopback;
	}

	public void setHeadless( final boolean headless ) {
		this.headless = headless;
	}

	public HeadlessCommand getHeadlessCmd() {
		return headlessCmd;
	}

	public boolean isShowHelp() {
		return showHelp;
	}
}
