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
	
	<#if ( class_properties_required?size > 0 ) || ( class_properties_all_optional?size > 0 ) ><#t>
	public convenience init(<#list class_properties_all_required_notvalued as property>${property.property_name}: ${property.property_type.type_literal}<#sep>, </#sep></#list>) {
		
		self.init(
			<#list class_properties_all as property>
			${property.property_name}: <#if property.property_type.type_optional>nil<#elseif property.property_value??>${property.property_value}<#else>${property.property_name}</#if><#sep>, </#sep>
			</#list>
		)
		
	}
	</#if>
	
	public <#if class_parent?? && class_properties?size==0 >override </#if>init(<#list class_properties_all as property>${property.property_name}: ${property.property_type.type_literal}<#sep>, </#sep></#list>) {
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