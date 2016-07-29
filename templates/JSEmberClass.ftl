//  
//  ${file_name}
//  ${entity_identifier}
//  ${product_name} (${model_version})
//  
//  Automatically generated on ${file_date} at ${file_time} by ${user}.
//  

import Model from 'ember-data/model';
import attr from 'ember-data/attr';
import { belongsTo } from 'ember-data/relationships';
import { hasMany } from 'ember-data/relationships';

export default Model.extend({
	
	<#list class_properties_raws as raw><#t>
	${raw.property_name}: attr(${raw.property_mapping})<#sep>,</#sep>
	</#list>
	
	<#list class_properties_relations as relation>
	${relation.property_name}: ${relation.relation_type}('${relation.property_dominant_type}')<#sep>,</#sep>
	</#list>
	
});