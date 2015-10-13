//  
//  ${file_name}
//  ${entity_identifier}
//  ${package_name}
//  
//  Automatically generated on ${file_date}.
//  

import Foundation
import ObjectMapper

class ${entity_name} : <#if class_parent??>${class_parent}, </#if>Mappable {
	
	<#list class_properties as property>
	var ${property.property_name} : ${property.property_type.type_name}
	</#list><#t>
	
	<#if class_parent??>override </#if>required init?(_ map: Map) {
		<#t>
		<#if class_parent??>
		
		super.init(map)
		</#if><#t>
		<#list class_properties as property><#t>
		<#if !property.property_type.type_optional><#t>
		${property.property_name} = map["${property.property_key}"].valueOrFail()<#t>
		</#if><#t>
		</#list><#t>
		
		if ( !map.isValid ) {
			return nil
		}
		
	}
	
	func mapping(map: Map) {
		
		<#list class_properties as property>
		${property.property_name} <- map["${property.property_key}"]	
		</#list>
		
	}
	
}