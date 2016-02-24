//  
//  ${file_name}
//  ${entity_identifier}
//  ${product_name}
//  
//  Automatically generated on ${file_date} at ${file_time} by ${user}.
//  

#import "${entity_name}.h"

@implementation ${entity_name}

+ (NSDictionary*)toDictionary {
	
	return @{
		<#list enum_values as value>
		@"${value.enum_value_rawValue}": @(${entity_name}Value${value.enum_value_name})<#sep>,</#sep>
		</#list>	
	};
	
}

+ (NSDictionary*)fromDictionary {
	
	return @{
		<#list enum_values as value>
		@(${entity_name}Value${value.enum_value_name}): @"${value.enum_value_rawValue}"<#sep>,</#sep>
		</#list>	
	};
	
}

- (id)initWithValue:(${entity_name}Value)value {
	
	self = [self init];
	
	if ( self ) {
		
		self.value = value;
		
	}
	
	return self;
	
}

- (void)setStringValue:(NSString*)stringValue {
	
	self.value = [[[${entity_name} toDictionary] objectForKey:stringValue] integerValue];
	
}

- (NSString*)stringValue {
	
	return [[${entity_name} fromDictionary] objectForKey:@(self.value)];
	
}

- (NSUInteger)hash {
	
	return self.stringValue.hash;
	
}

- (BOOL)isEqualTo${entity_name}:(${entity_name}*)other {
	
	return other!=nil && other.value == self.value;
	
}

- (BOOL)isEqual:(${entity_name}*)other {
	
	return [other isKindOfClass:[${entity_name} class]] && [self isEqualTo${entity_name}:other];
	
}


@end