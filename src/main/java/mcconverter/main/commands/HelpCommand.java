package mcconverter.main.commands;

import mcconverter.main.Format;
import mcconverter.main.Main;

public class HelpCommand extends Command {
	
	/* ===== Constants ===== */
	
	public static final String CommandName = "help";
	
	
	
	/* ===== Construction ===== */
	
	public HelpCommand() {
		
		super(CommandName, 0);
		
	}
	
	
	
	/* ===== Public Functions ===== */
	
	public void perform() {
		
		String output = "MCConverter " + Main.Version + "\n";
		
		output += "Usage: ";
		
		output += Format.format("mcconverter.main.Main", Format.Dark, Format.Green) + "\n";
		
		output += argumentHelp(HelpCommand.CommandName, "Displays usage information");
		output += argumentHelp(FormattedCommand.CommandName, "Formats the output if set");
		output += argumentHelp(ConfigurationCommand.CommandName, "Sets the location of the configuration file", "path");
		
		System.out.println(output);
		Main.exit();
		
	}
	
	
	/* ===== Private Functions ===== */
	
	private String argumentHelp(String name, String description, String... arguments) {
		
		String output = "\t" + Format.format(Command.CommandPrefix + name, Format.Light, Format.Blue);
		
		for ( String argument : arguments ) {
			
			output += " [" + Format.format(argument, Format.Light, Format.Blue) + "]";
			
		}
		
		output += " : " + description + "\n";
		
		return output;
		
	}
	
	
}
