//  
//  ${file_name}
//  ${entity_identifier}
//  ${product_name}
//  
//  Automatically generated on ${file_date} at ${file_time}.
//  

import Foundation

public enum ${entity_name}Enum : Int {
	
	<#list enum_values as value>
	case ${value.enum_value_name}
	</#list>
	
	public static let AllEnumValues = [
		<#list enum_values as value>
		${value.enum_value_name}<#sep>,</#sep>
		</#list>
	]
	
	public var stringValue : String {
		switch ( self ) {
		<#list enum_values as value>
		case ${value.enum_value_name}:
			return "${value.enum_value_rawValue}"
		</#list>
		}
	}
	
	public var wrapper : ${entity_name} {
        
        return ${entity_name}(value: self)
        
    }
	
}

public class ${entity_name} : NSObject {
	
	<#list enum_values as value>
	static let ${value.enum_value_name} = ${entity_name}Enum.${value.enum_value_name}.wrapper
	</#list>
	
	public var value : ${entity_name}Enum?
	
	public var stringValue : String {
		
		get {
			return value?.stringValue ?? ""		
		}
		set ( v ) {
			
			value = nil
			
			for current in ${entity_name}Enum.AllEnumValues {
				
				if ( current.stringValue == v ) {
					
					value = current
					break
					
				}
				
			}
			
		}
		
	}
	
	public var integerValue : Int {
		
		get {
			return value?.rawValue ?? 0
		}
		
		set ( v ) {
			value = ${entity_name}Enum(rawValue: v)
		}
		
	}
	
	public override var hash : Int {
		
		return value?.stringValue.hash ?? 0
		
	}
	
	public init(stringValue: String) {
		
		super.init()
		self.stringValue = stringValue
		
	}
	
	public init(integerValue: Int) {
		
		super.init()
		self.integerValue = integerValue
		
	}
	
	public init(value: ${entity_name}Enum) {
		
		self.value = value
		
	}
	
	public func isEqualTo${entity_name}(other: ${entity_name}?) -> Bool {
		
		return value?.rawValue == other?.value?.rawValue
		
	}
	
	public override func isEqual(object: AnyObject?) -> Bool {
		
		return isEqualTo${entity_name}(object as? ${entity_name})
		
	}
	
}