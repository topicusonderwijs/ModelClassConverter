package mcconverter.main.commands;

import mcconverter.main.Main;

public class ConfigurationCommand extends Command {
	
	/* ===== Constants ===== */
	
	public static final String CommandName = "configuration";
	
	
	/* ===== Construction ===== */
	
	public ConfigurationCommand() {
		
		super(CommandName, 1);
		
	}
	
	
	/* ===== Public Functions ===== */
	
	public void perform() {
		
		Main.ConfigurationLocation = getArgument(0);
		
	}
	
}
