//  
//  ${file_name}
//  ${entity_identifier}
//  ${product_name}
//  
//  Automatically generated on ${file_date} at ${file_time}.
//  

#import "${entity_name}.h"


@implementation ${entity_name}

- (id)init {
	
	self = [super init];
	
	if ( self ) {
		
		
		
	}
	
	return self;
	
}

- (RKObjectMapping*)requestMapping {
	
	RKObjectMapping *mapping = [RKObjectMapping mappingForClass:[NSMutableDictionary class]];
	<#if class_parent??>
	
	[mapping addAttributeMappingsFromArray:[[super requestMapping] attributeMappings]];
	</#if>
	<#if ( class_properties_natives?size > 0)><#t>
	
	[mapping addAttributeMappingsFromDictionary:@{
		<#list class_properties_natives as property>
		@"${property.property_name}": @"${property.property_key}"<#sep>,</#sep>
		</#list>
	}];
	</#if>
	<#if ( class_properties_relations?size > 0)><#t>
	
	[mapping addPropertyMappingsFromArray:@[
		<#list class_properties_relations as property>
		[RKRelationshipMapping relationshipMappingFromKeyPath:@"${property.property_key}" toKeyPath:@"${property.property_name}" withMapping:[${property.property_type.type_name} dynamicRequestMapping]]<#sep>,</#sep>
		</#list>
	]];
	</#if>
	
	return mapping;
	
}

+ (RKDynamicMapping*)dynamicRequestMapping {
	
	RKDynamicMapping* mapping = [[RKDynamicMapping alloc] init];
	
	[mapping setObjectMappingForRepresentationBlock:^RKObjectMapping *(id representation) {
		return [representation requestMapping];
	}];
	
	return mapping;
	
}

+ (RKObjectMapping*)responseMapping {
	
	RKObjectMapping* mapping = [RKObjectMapping mappingForClass:[${entity_name} class]];
	<#if class_parent??>
	
	[mapping addAttributeMappingsFromArray:[[${class_parent} responseMapping] attributeMappings]];
	</#if>
	<#if ( class_properties_natives?size > 0)><#t>
	
	[mapping addAttributeMappingsFromDictionary:@{
		<#list class_properties_natives as property>
		@"${property.property_key}": @"${property.property_name}"<#sep>,</#sep>
		</#list>
	}];
	</#if>
	<#if ( class_properties_relations?size > 0)><#t>
	
	[mapping addPropertyMappingsFromArray:@[
		<#list class_properties_relations as property>
		[RKRelationshipMapping relationshipMappingFromKeyPath:@"${property.property_name}" toKeyPath:@"${property.property_key}" withMapping:[${property.property_type.type_name} dynamicResponseMapping]]<#sep>,</#sep>
		</#list>
	]];
	</#if>
	
	return mapping;
	
}

+ (RKMapping*)dynamicResponseMapping {
	
	return [${entity_name} responseMapping];
	
}

+ (NSString*)typeName {
	
	return @"${entity_descriptor}";
	
}

@end
