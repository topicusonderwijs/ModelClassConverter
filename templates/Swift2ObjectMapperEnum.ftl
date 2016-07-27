//  
//  ${file_name}
//  ${entity_identifier}
//  ${product_name} (${model_version})
//  
//  Automatically generated on ${file_date} at ${file_time} by ${user}.
//  


import Foundation

public enum ${entity_name} : String {
	
	<#list enum_values as value>
	case ${value.enum_value_name}<#if value.enum_value_rawName??> = "${value.enum_value_rawName}"</#if>
	</#list>
	
}