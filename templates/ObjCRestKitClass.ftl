//  
//  ${file_name}
//  ${entity_identifier}
//  ${product_name} (${model_version})
//  
//  Automatically generated on ${file_date} at ${file_time} by ${user}.
//  

#import "${entity_name}.h"


@implementation ${entity_name}

static RKObjectMapping* responseMapping;

- (id)init {
	
	self = [super init];
	
	if ( self ) {
		
		<#list class_properties_valued><#t>
		<#items as property>
		self.${property.property_name} = ${property.property_value};
		</#items>
		
		</#list><#t>
		self = [EntityRegistry initializeEntity:self];
		
	}
	
	return self;
	
}

- (RKObjectMapping*)requestMapping {
	
	RKObjectMapping *mapping = [RKObjectMapping mappingForClass:[NSMutableDictionary class]];
	<#if class_parent??>
	
	[mapping addPropertyMappingsFromArray:[[NSArray alloc] initWithArray:[[super requestMapping] propertyMappings] copyItems:true]];
	</#if>
	<#if ( class_properties_natives?size > 0)><#t>
	
	[mapping addAttributeMappingsFromDictionary:@{
		<#list class_properties_natives as property>
		@"${property.property_path}": @"${property.property_key}"<#sep>,</#sep>
		</#list>
	}];
	</#if>
	<#if ( class_properties_relations?size > 0)><#t>
	
	[mapping addPropertyMappingsFromArray:@[
		<#list class_properties_relations as property>
		[RKRelationshipMapping relationshipMappingFromKeyPath:@"${property.property_key}" toKeyPath:@"${property.property_name}" withMapping:[${property.property_dominant_type} dynamicRequestMapping]]<#sep>,</#sep>
		</#list>
	]];
	</#if>
	
	return mapping;
	
}

+ (RKMapping*)dynamicRequestMapping {
	
	return [EntityRegistry requestMappingFor:[self class]];
	
}

+ (RKObjectMapping*)responseMapping {
	
	if ( responseMapping == nil ) {
		
		responseMapping = [RKObjectMapping mappingForClass:[${entity_name} class]];
		<#if class_parent_literal??>
		
		[responseMapping addPropertyMappingsFromArray:[[NSArray alloc] initWithArray:[[${class_parent_literal} responseMapping] propertyMappings] copyItems:true]];
		</#if>
		<#if ( class_properties_natives?size > 0)><#t>
		
		[responseMapping addAttributeMappingsFromDictionary:@{
			<#list class_properties_natives as property>
			@"${property.property_key}": @"${property.property_path}"<#sep>,</#sep>
			</#list>
		}];
		</#if>
		<#if ( class_properties_relations?size > 0)><#t>
		
		[responseMapping addPropertyMappingsFromArray:@[
			<#list class_properties_relations as property>
			[RKRelationshipMapping relationshipMappingFromKeyPath:@"${property.property_name}" toKeyPath:@"${property.property_key}" withMapping:[${property.property_dominant_type} dynamicResponseMapping]]<#sep>,</#sep>
			</#list>
		]];
		</#if>
		
	}
	
	return responseMapping;
	
}

+ (RKMapping*)dynamicResponseMapping {
	
	return [EntityRegistry responseMappingFor:[self class]];
	
}

+ (NSString*)descriptor {
	
	return @"${entity_descriptor}";
	
}

@end
