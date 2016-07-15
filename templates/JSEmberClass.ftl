//  
//  ${file_name}
//  ${entity_identifier}
//  ${product_name} (${model_version})
//  
//  Automatically generated on ${file_date} at ${file_time} by ${user}.
//  

import Model from 'ember-data/model';
import attr from 'ember-data/attr';

export default Model.extend({
	<#list class_properties as property><#t>
	${property.property_name}: attr(${property.property_mapping})<#sep>,</#sep>
	</#list>
});