//  
//  ${file_name}
//  ${product_name} (${model_version})
//  
//  Automatically generated on ${file_date} at ${file_time} by ${user}.
//  


import Foundation
import ObjectMapper

public class EntityRegistry {
	
	private static let descriptors : [String: Mappable.Type] = [
		<#list package_classes><#items as class>
		${class.entity_name}.descriptor: ${class.entity_name}.self<#sep>,</#sep>
		</#items><#else>
		:
		</#list>
	]
	
	public static let Version = "${model_version}"
	
	public class func toType(descriptor: String) -> Mappable.Type? {
		
		return descriptors[descriptor]
		
	}
	
	public class func fromType(type: Mappable.Type) -> String? {
		
		var descriptor : String?
        
        for (key, value) in descriptors {
            
            if ( value == type ) {
                
                descriptor = key
                break
                
            }
            
        }
		
        return descriptor
		
	}
	
}

extension DateFormatterTransform {
	
	internal convenience init(format: String) {
		
		let formatter = NSDateFormatter()
		formatter.dateFormat = format
		
		self.init(dateFormatter: formatter)
		
	}
	
}

extension Map {
	
	<#list package_transforms?keys as name>
	internal static let ${name}Transform = ${package_transforms[name]}
	</#list>
	
}


