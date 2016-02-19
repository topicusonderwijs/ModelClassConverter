//  
//  ${file_name}
//  ${product_name}
//  
//  Automatically generated on ${file_date} at ${file_time}.
//  

import Foundation

public class EntityRegistry : NSObject {
	
	public static let registry = [
		<#list package_classes as class>
		"${class.entity_descriptor}": ${class.entity_name}.responseMapping<#sep>,</#sep>
		</#list>
	]
	
	public static func mappingFor(type: String) -> RKObjectMapping? {
		
		return registry[type]
		
	}
	
}