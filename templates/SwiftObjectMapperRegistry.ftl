//  
//  ${file_name}
//  ${product_name} (${model_version})
//  
//  Automatically generated on ${file_date} at ${file_time} by ${user}.
//  


import Foundation
import ObjectMapper

public class EntityRegistry {
	
	private static let registry : [String: Mappable.Type] = [
		<#list package_classes as class>
		"${class.entity_descriptor}": ${class.entity_name}.self<#sep>,</#sep>
		</#list>
	]
	
}