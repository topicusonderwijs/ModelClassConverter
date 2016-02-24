//  
//  ${file_name}
//  ${entity_identifier}
//  ${product_name}
//  
//  Automatically generated on ${file_date} at ${file_time} by ${user}.
//  

import Foundation

public class ${entity_name}<#if ( class_type.type_parameters?size > 0)> <<#list class_type.type_parameters as parameter><#t>
${parameter.parameter_literal}<#t></#list>></#if> : <#if class_parent??>${class_parent}<#else>NSObject</#if><#list class_type.type_protocols as protocol>, ${protocol.type_name}</#list> {
	<#t>
	<#if ( class_constants?size > 0)>
	
	public struct Constants {
	<#list class_constants as constant>
		${constant.property_literal}
	</#list><#t>
	}
	</#if><#t>
	<#if ( class_properties?size > 0)><#t>
	
	<#list class_properties as property>
	public ${property.property_literal}
	</#list><#t>
	<#t>
	</#if>
	
	public <#if class_parent??>override </#if>var requestMapping : RKObjectMapping {
		
		let mapping = RKObjectMapping(forClass: ${entity_name}.self)
		<#if class_parent??>
		
		mapping.addAttributeMappingsFromArray(${class_parent}.responseMapping.attributeMappings)
		</#if>
		<#if ( class_properties_natives?size > 0)><#t>
		
		mapping.addAttributeMappingsFromDictionary([
			<#list class_properties_natives as property>
			"${property.property_key}": "${property.property_name}"<#sep>,</#sep>
			</#list>
		])
		</#if>
		<#if ( class_properties_relations?size > 0)><#t>
		
		mapping.addPropertyMappingsFromArray([
			<#list class_properties_relations as property>
			RKRelationshipMapping(
				fromKeyPath: "${property.property_key}",
				toKeyPath: "${property.property_name}",
				withMapping: ${property.property_type.type_name}.dynamicResponseMapping
			)<#sep>,</#sep>
			</#list>
		])
		</#if>
		
		return mapping
		
	}
	
	public <#if class_parent??>override </#if>class var responseMapping : RKObjectMapping {
		
		let mapping = RKObjectMapping(forClass: ${entity_name}.self)
		<#if class_parent??>
		
		mapping.addAttributeMappingsFromArray(${class_parent}.responseMapping.attributeMappings)
		</#if>
		<#if ( class_properties_natives?size > 0)><#t>
		
		mapping.addAttributeMappingsFromDictionary([
			<#list class_properties_natives as property>
			"${property.property_name}": "${property.property_key}"<#sep>,</#sep>
			</#list>
		])
		</#if>
		<#if ( class_properties_relations?size > 0)><#t>
		
		mapping.addPropertyMappingsFromArray([
			<#list class_properties_relations as property>
			RKRelationshipMapping(
				fromKeyPath: "${property.property_name}",
				toKeyPath: "${property.property_key}",
				withMapping: ${property.property_type.type_name}.dynamicResponseMapping
			)<#sep>,</#sep>
			</#list>
		])
		</#if>
		
		return mapping
		
	}
	
	public <#if class_parent??>override </#if>class var dynamicResponseMapping : RKMapping {
		
		return responseMapping
		
	}
	
	public <#if class_parent??>override </#if>class var typeName : String {
		
		return "${entity_descriptor}"
		
	}
	
}