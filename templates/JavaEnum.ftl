//  
//  ${file_name}
//  ${entity_identifier}
//  ${product_name} (${model_version})
//  
//  Automatically generated on ${file_date} at ${file_time} by ${user}.
//  

public enum ${entity_name} {
	<#list enum_values>
	
	<#items as value>
	${value.enum_value_name}<#sep>, </#sep><#if value?is_last >;</#if>
	</#items>
	</#list>
	<#--<#list class_properties>
	
	<#items as property>
	private ${property.property_literal};
	</#items>
	</#list>
	<#if (class_properties?size>0) >
	
	public ${entity_name}(<#list class_properties as property>${property.property_literal}<#sep>, </#sep></#list>) {
		
		<#list class_properties as property>
		this.${property.property_name} = ${property.property_name};
		</#list>
		
	}
	
	</#if>
	<#list class_properties>
	
	<#items as property>
	
	public ${property.property_type.type_literal} get${property.property_name?cap_first}() {
		
		return ${property.property_name};
		
	}
	</#items>
	</#list><#t>
	-->
}