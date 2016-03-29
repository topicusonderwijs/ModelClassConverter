//  
//  ${file_name!}
//  ${entity_identifier!}
//  ${product_name!} (${model_version!})
//  
//  Automatically generated on ${file_date} at ${file_time} by ${user}.
//  

package ${product_package!};

import java.io.Serializable;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.net.URI;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

public class ${entity_name}<#list class_type.type_parameters><<#items as parameter><#t>
${parameter.parameter_literal}<#t><#sep>, </#sep></#items>></#list><#if class_parent_literal??> extends ${class_parent_literal}</#if> implements Serializable {
	
	public static final String descriptor = "${entity_descriptor}";
	<#list class_constants>
	
	<#items as constant>
	public ${constant.property_literal};
	</#items>
	</#list><#t>
	<#list class_properties><#t>
	
	<#items as property>
	private ${property.property_literal};
	</#items>
	</#list><#t>
	<#list class_properties><#t>
	<#items as property>
	
	public void set${property.property_name?cap_first}(${property.property_type.type_literal} ${property.property_name}) {
		
		this.${property.property_name} = ${property.property_name};
		
	}
	
	public ${property.property_type.type_literal} get${property.property_name?cap_first}() {
		
		return ${property.property_name};
		
	}
	</#items>
	</#list><#t>
	
}