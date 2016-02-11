//  
//  ${file_name}
//  ${entity_identifier}
//  ${product_name}
//  
//  Automatically generated on ${file_date} at ${file_time}.
//  

import Foundation

public enum ${entity_name}Enum : String {
	
	<#list enum_values as value>
	case ${value.enum_value_name} = "${value.enum_value_rawValue}"
	</#list>
	
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
			return value?.rawValue ?? ""
		}
		
		set ( v ) {
			value = ${entity_name}Enum(rawValue: v)
		}
		
	}
	
	public override var hash : Int {
		
		return value?.rawValue.hash ?? 0
		
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