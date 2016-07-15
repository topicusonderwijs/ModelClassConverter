//  
//  ${file_name}
//  ${entity_identifier}
//  ${product_name} (${model_version})
//  
//  Automatically generated on ${file_date} at ${file_time} by ${user}.
//  

#ifndef ${entity_name}Header
#define ${entity_name}Header

typedef NS_ENUM(NSInteger, ${entity_name}Value) {
	<#list enum_values as value>
	${entity_name}Value${value.enum_value_name}<#sep>,</#sep>
	</#list>
};

@interface ${entity_name} : NSObject

- (id)initWithValue:(${entity_name}Value)value;

- (BOOL)isEqualTo${entity_name}:(${entity_name}*)other;

@property ${entity_name}Value value;
@property (nonatomic, strong) NSString* stringValue;
@property (nonatomic, readonly) NSString* rawValue;

@end

#endif
