//  
//  ${file_name}
//  ${entity_identifier}
//  ${product_name} (${model_version})
//  
//  Automatically generated on ${file_date} at ${file_time} by ${user}.
//  

var ${entity_name} = {
	<#list enum_values as value><#t>
	${value.enum_value_name}: "${value.enum_value_rawValue}"<#sep>, </#sep>
	</#list>
};
