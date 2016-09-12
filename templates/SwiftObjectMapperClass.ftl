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
${parameter.parameter_literal}<#t><#sep>, </#sep></#list>> </#if>: <#if class_parent_literal??>${class_parent_literal}<#else>Mappable</#if> {
	<#t>
	<#if ( class_constants?size > 0 ) >
	
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
	
	public <#if class_parent?? && class_properties?size==0 >override </#if>init(<#list class_properties_all as property>${property.property_name}: ${property.property_type.type_literal}<#if property.property_value??> = ${property.property_value}<#elseif property.property_type.type_optional> = nil</#if><#sep>, </#sep></#list>) {
		<#list class_properties>
		
		<#items as property>
		self.${property.property_name} = ${property.property_name}
		</#items></#list><#t>
		<#if class_parent??>
		
		super.init(<#list class_properties_inherited as property>${property.property_name}: ${property.property_name}<#sep>, </#sep></#list>)
		</#if>
		
	}
	
	public required init?(_ map: Map) {
		<#list class_properties_required><#t>
		
		<#items as property>
		var ${property.property_name}: ${property.property_type.type_literal}?
		${property.property_mapping}
		</#items>
		</#list><#t>
		<#list class_properties_required><#t>
		
		if <#items as property>let ${property.property_name} = ${property.property_name}<#sep>, </#sep></#items> {
			<#list class_properties_required as property>
			self.${property.property_name} = ${property.property_name}
			</#list>
		} else {
			print("Warning: Could not map to ${entity_name}")
			return nil
		}
		</#list><#if class_parent??>
		
		super.init(map)
		</#if><#t>
		
	}
	
	public <#if class_parent??>override </#if>func mapping(_ map: Map) {
		<#if class_parent??>
		
		super.mapping(map)
		</#if><#t>
		<#list class_properties><#t>
		
		<#items as property>
		${property.property_mapping}
		</#items>
		</#list>
		
	}
	
	public <#if class_parent??>override </#if>class var descriptor : String {
		
		return "${entity_descriptor}"
		
	}
	
}