package mcconverter.configuration;

public class CustomEntity {
	
	/* ===== Private Properties ===== */
	
	private String name;
	private String rename;
	private boolean ignored;
	private CustomType type;
	
	
	
	/* ===== Public Functions ===== */
	
	public String getName() {
		
		return name;
		
	}
	
	public boolean hasName() {
		
		return getName() != null;
		
	}
	
	public void setName(String name) {
		
		this.name = name;
		
	}
	
	public boolean hasRename() {
		
		return getRename() != null;
		
	}
	
	public String getRename() {
		
		return rename;
		
	}
	
	public void setRename(String rename) {
		
		this.rename = rename;
		
	}
	
	public boolean isIgnored() {
		
		return ignored;
		
	}
	
	public void setIgnored(boolean ignored) {
		
		this.ignored = ignored;
		
	}
	
	public boolean hasType() {
		
		return getType() != null;
		
	}
	
	public CustomType getType() {
		
		return type;
		
	}
	
	public void setType(CustomType type) {
		
		this.type = type;
		
	}
	
}
