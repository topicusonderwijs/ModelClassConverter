//  
//  ${file_name}
//  ${entity_identifier}
//  ${product_name}
//  
//  Automatically generated on ${file_date}.
//  

import Foundation

enum ${entity_name} : String {
	
	<#list enum_values as value>
	case ${value.enum_value_name}<#if value.enum_value_rawValue??> = "${value.enum_value_rawValue}" </#if>
	</#list>
	
}