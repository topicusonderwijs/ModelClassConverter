//  
//  ${file_name}
//  ${product_name} (${model_version})
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

+ (NSString*)version {
	
	return @"${model_version}";
	
}

+ (void)setDelegate:(id<EntityRegistryDelegate>)registryDelegate {
	
	delegate = registryDelegate;
	
	NSAssert([[self delegate] validateVersion:[self version]], @"EntityRegistry is generated with invalid version");
	
}

+ (BOOL)hasDelegate {
	
	return delegate != nil;
	
}

+ (id<EntityRegistryDelegate>)delegate {
	
	NSAssert([self hasDelegate], @"EntityRegistry requires a delegate");
	
	return delegate;
	
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
	
	return [[self delegate] responseMappingFor:c];
	
}

+ (RKMapping*)requestMappingFor:(Class)c {
	
	return [[self delegate] requestMappingFor:c];
	
}

+ (id)initializeEntity:(id)entity {
	
	return [[self delegate] initializeEntity:entity];
	
}

@end
