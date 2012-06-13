package org.blockout;

import com.beust.jcommander.Parameter;

/**
 * Container class for all command line arguments parsed by JCommander.
 * 
 * @author Marc-Christian Schulze
 * 
 */
public class Arguments {
	@Parameter(names = { "--headless" })
	private boolean	headless;

	@Parameter(names = { "-h", "--help" })
	private boolean	showHelp;

	public boolean isShowHelp() {
		return showHelp;
	}

	public boolean isHeadless() {
		return headless;
	}
}
