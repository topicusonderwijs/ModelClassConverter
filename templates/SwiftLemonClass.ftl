//  
//  ${file_name}
//  ${entity_identifier}
//  ${product_name} (${model_version})
//  
//  Automatically generated on ${file_date} at ${file_time} by ${user}.
//  

import Lemon

public class ${entity_name} <#if ( class_type.type_parameters?size > 0)><<#list class_type.type_parameters as parameter><#t>
${parameter.parameter_literal}<#t><#sep>, </#sep></#list>> </#if>: <#if class_parent_literal??>${class_parent_literal}<#else>JSONReadable, JSONWritable</#if> {
	<#t>
	<#if ( class_constants?size > 0 ) >
	
	// MARK: - Constants
	
	public struct Constants {
		<#list class_constants as property>
		public ${property.property_literal}
		</#list><#t>
	}
	
	</#if><#t>
	
	// MARK: - Properties
	
	public override class var descriptor : String {
		return "${entity_descriptor}"
	}
	<#list class_properties><#t>
	
	<#items as property>
	public ${property.property_literal}
	</#items>
	</#list><#t>
	
	
	
	// MARK: - Construction
	
	public <#if class_parent?? && class_properties?size==0 >override </#if>init(<#list class_properties_all as property>${property.property_name}: ${property.property_type.type_literal}<#if property.property_value??> = ${property.property_value}<#elseif property.property_type.type_optional> = nil</#if><#sep>, </#sep></#list>) {
		<#list class_properties>
		
		<#items as property>
		self.${property.property_name} = ${property.property_name}
		</#items></#list><#t>
		<#if class_parent??>
		
		super.init(<#list class_properties_inherited as property>${property.property_name}: ${property.property_name}<#sep>, </#sep></#list>)
		</#if>
		
	}
	
	public required init(_ from: JSONReader) throws {
		<#list class_properties><#t>
		
		<#items as property>
		${property.property_name} = try<#if property.property_type.type_optional >?</#if> from.get(${property.property_reading})
		</#items>
		</#list><#t>
		<#if class_parent??>
		
		try super.init(from)
		</#if><#t>
		
	}
	
	
	
	// MARK: - Functions
	
	
	// MARK: JSONWritable Functions
	
	public <#if class_parent??>override </#if>func write(to: JSONWriter) {
		<#list class_properties><#t>
		
		<#items as property>
		to.set(${property.property_writing})
		</#items>
		
		</#list>
		<#if class_parent??>
		super.write(to: to)
		
		</#if>
	}
	
}