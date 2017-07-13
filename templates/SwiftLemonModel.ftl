//  
//  ${file_name}
//  ${product_name} (${model_version})
//  
//  Automatically generated on ${file_date} at ${file_time} by ${user}.
//  

import Lemon

public class ${product_name}Model {
	
	public static func setup() {
		
		Linkable.register(
			<#list package_classes as class><#t>
			${class.entity_name}.self<#sep>, </#sep>
			</#list>
		)
		
	}
	
}
