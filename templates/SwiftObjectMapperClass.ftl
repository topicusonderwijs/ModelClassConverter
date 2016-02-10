//  
//  ${file_name}
//  ${entity_identifier}
//  ${product_name}
//  
//  Automatically generated on ${file_date}.
//  

import Foundation
import ObjectMapper

class ${entity_name} <#if ( class_type.type_parameters?size > 0)><<#list class_type.type_parameters as parameter><#t>
${parameter.parameter_literal}<#t><#sep>, </#sep></#list>> </#if>: <#if class_parent??>${class_parent}, </#if>Mappable {
	<#t>
	<#if ( class_constants?size > 0)>
	
	struct Constants {
	<#list class_constants as property>
		${property.property_literal}
	</#list><#t>
	}
	</#if><#t>
	<#if ( class_properties?size > 0)><#t>
	
	<#list class_properties as property>
	${property.property_literal}
	</#list><#t>
	<#t>
	</#if>
	
	<#if class_parent??>override </#if>required init?(_ map: Map) {
	
		<#if class_parent??>
		super.init(map)
		</#if><#t>
		<#list class_properties as property><#t>
		<#if !property.property_type.type_optional><#t>
		${property.property_name} = map["${property.property_key}"].valueOrFail()
		</#if><#t>
		</#list><#t>
		
		if ( !map.isValid ) {
			return nil
		}
		
	}
	
	<#if class_parent??>override </#if>func mapping(map: Map) {
		<#if class_parent??>
		
		super.mapping(map)
		</#if><#t>
		<#if ( class_properties?size > 0)><#t>
		
		<#list class_properties as property>
		${property.property_mapping}
		</#list>
		</#if>
		
	}
	
}