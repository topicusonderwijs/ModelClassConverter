//  
//  ${file_name}
//  ${entity_identifier}
//  ${product_name} (${model_version})
//  
//  Automatically generated on ${file_date} at ${file_time} by ${user}.
//  

<#list class_imports as import>
import ${import};
</#list>

export default <#if class_parent_literal??>${class_parent_literal}<#else>Model</#if>.extend({
	
	<#list class_properties_raws as raw><#t>
	${raw.property_name}: attr(${raw.property_mapping})<#sep>,
	</#sep></#list><#if ( class_properties_raws?size > 0 && class_properties_relations?size > 0 ) ><#t>,
	</#if>
	
	<#list class_properties_relations as relation>
	${relation.property_name}: ${relation.relation_type}('${relation.property_dominant_type}')<#sep>,</#sep>
	</#list>
	
});