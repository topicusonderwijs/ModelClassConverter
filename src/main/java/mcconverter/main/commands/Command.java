package mcconverter.main.commands;

import java.util.ArrayList;
import java.util.List;

public abstract class Command {
	
	/* ===== Constants ===== */
	
	public static final String CommandPrefix = "--";
	
	
	
	/* ===== Private Properties ===== */
	
	private String name;
	private int argumentCount;
	private List<String> arguments;
	
	
	
	/* ===== Construction ===== */
	
	protected Command(String name, int argumentCount) {
		
		this.name = name;
		this.argumentCount = argumentCount;
		this.arguments = new ArrayList<String>();
		
	}
	
	
	
	/* ===== Public Functions ===== */
	
	public final String getName() {
		
		return name;
		
	}
	
	public final int getArgumentCount() {
		
		return argumentCount;
		
	}
	
	public final boolean addArgument(String argument) {
		
		boolean added = false;
		
		if ( arguments.size() < getArgumentCount() ) {
			
			added = arguments.add(argument);
			
		}
		
		if ( added ) {
			
			tryPerform();
			
		}
		
		return added;
		
	}
	
	public String getArgument(int index) {
		
		return getArguments().get(index);
		
	}
	
	public final List<String> getArguments() {
		
		return arguments;
		
	}
	
	public void reset() {
		
		arguments.clear();
		
		tryPerform();
		
	}
	
	public abstract void perform();
	
	
	
	/* ===== Private Functions ===== */
	
	private void tryPerform() {
		
		if ( arguments.size() == getArgumentCount() ) {
			
			perform();
			
		}
		
	}
	
	
}
