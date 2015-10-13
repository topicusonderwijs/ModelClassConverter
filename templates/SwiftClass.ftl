//  
//  ${file_name}
//  ${entity_identifier}
//  Digdag
//  
//  Automatically generated on ${file_date}.
//  

import Foundation

class ${entity_name} <#if class_parent??>: ${class_parent} </#if>{
	
	<#list class_properties as property>
	var ${property.entity_name} : ${property.property_type}
	</#list>
	
}