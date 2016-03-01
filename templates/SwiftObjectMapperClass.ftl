//  
//  ${file_name}
//  ${entity_identifier}
//  ${product_name} (${model_version})
//  
//  Automatically generated on ${file_date} at ${file_time} by ${user}.
//  


import Foundation
import ObjectMapper

public class ${entity_name} <#if ( class_type.type_parameters?size > 0)><<#list class_type.type_parameters as parameter><#t>
${parameter.parameter_literal}<#t><#sep>, </#sep></#list>> </#if>: <#if class_parent??>${class_parent}<#else>Mappable</#if> {
	<#t>
	<#if ( class_constants?size > 0)>
	
	public struct Constants {
		<#list class_constants as property>
		public ${property.property_literal}
		</#list><#t>
	}
	</#if><#t>
	<#list class_properties><#t>
	
	<#items as property>
	public ${property.property_literal}
	</#items>
	</#list><#t>
	<#t>
	
	<#if class_initialize>
	public <#if class_parent??>override </#if>init() {
		<#list class_properties_valued>
		
		<#items as property>
		${property.property_name} = ${property.property_value}
		</#items></#list><#t>
		<#if class_parent??>
		
		super.init()
		</#if>
		
	}
	</#if>
	
	public required init?(_ map: Map) {
		<#list class_properties_required><#t>
		
		<#items as property>
		${property.property_name} = map["${property.property_key}"].valueOrFail()
		</#items>
		</#list><#t>
		<#if class_parent??>
		
		super.init(map)
		</#if><#t>
		<#if ( class_properties_required?size > 0)>
		
		if ( !map.isValid ) {
			return nil
		}
		</#if>
		
	}
	
	public <#if class_parent??>override </#if>func mapping(map: Map) {
		<#if class_parent??>
		
		super.mapping(map)
		</#if><#t>
		<#list class_properties><#t>
		
		<#items as property>
		${property.property_name} <- map["${property.property_key}"]
		</#items>
		</#list>
		
	}
	
	public <#if class_parent??>override </#if>class var descriptor : String {
		
		return "${entity_descriptor}"
		
	}
	
}