//  
//  ${file_name}
//  ${product_name}
//  
//  Automatically generated on ${file_date} at ${file_time}.
//  

#import "EntityRegistry.h"
<#list package_classes as class>
#import "${class.entity_name}.h"
</#list>

static NSDictionary* registry = nil;

@implementation EntityRegistry

+ (RKObjectMapping*)mappingFor:(NSString*)type {
	
	if ( registry == nil ) {
		
		NSMutableDictionary* r = [NSMutableDictionary dictionary];
		
		<#list package_classes as class>
		[r setObject:[${class.entity_name} responseMapping] forKey:@"${class.entity_descriptor}"];
		</#list>
		
		registry = r;
		
	}
	
	return [registry objectForKey:type];
	
}

@end
