package mcconverter.main.commands;

import mcconverter.main.Format;

public class FormattedCommand extends Command {

	/* ===== Constants ===== */
	
	public static final String CommandName = "formatted";
	
	
	
	/* ===== Construction ===== */
	
	public FormattedCommand() {
		
		super(CommandName, 0);
		
	}
	
	
	
	/* ===== Public Functions ===== */
	
	public void perform() {
		
		Format.Enabled = true;
		
	}
	
}
