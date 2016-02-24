//  
//  ${file_name}
//  ${product_name}
//  
//  Automatically generated on ${file_date} at ${file_time} by ${user}.
//  

#import "EntityRegistry.h"
<#list package_classes as class>
#import "${class.entity_name}.h"
</#list>

static NSDictionary* registry = nil;
static id<EntityRegistryDelegate> delegate = nil;

@implementation EntityRegistry

+ (void)setDelegate:(id<EntityRegistryDelegate>)registryDelegate {
	
	delegate = registryDelegate;
	
}

+ (Class)classForDescriptor:(NSString*)descriptor {
	
	if ( registry == nil ) {
		
		NSMutableDictionary* r = [NSMutableDictionary dictionary];
		
		<#list package_classes as class>
		[r setObject:[${class.entity_name} class] forKey:@"${class.entity_descriptor}"];
		</#list>
		
		registry = r;
		
	}
	
	return [registry objectForKey:descriptor];
	
}

+ (RKMapping*)responseMappingFor:(Class)c {
	
	RKMapping* mapping = nil;
	
	if ( delegate != nil ) {
		
		mapping = [delegate responseMappingFor:c];
		
	}
	
	return mapping;
	
}

+ (RKMapping*)requestMappingFor:(Class)c {
	
	RKMapping* mapping = nil;
	
	if ( delegate != nil ) {
		
		mapping = [delegate requestMappingFor:c];
		
	}
	
	return mapping;
	
}

+ (id)initializeEntity:(id)entity {
	
	if ( delegate != nil ) {
		
		entity = [delegate initializeEntity:entity];
		
	}
	
	return entity;
	
}

@end
