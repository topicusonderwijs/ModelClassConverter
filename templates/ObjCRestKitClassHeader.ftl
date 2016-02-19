//  
//  ${file_name}
//  ${entity_identifier}
//  ${product_name}
//  
//  Automatically generated on ${file_date} at ${file_time}.
//  

#ifndef ${entity_name}Header
#define ${entity_name}Header

#import <Foundation/Foundation.h>
#import <RestKit/RestKit.h>
<#if class_parent??>#import "${class_parent}.h"</#if>
<#list class_imports as import>
#import "${import}.h"
</#list>

@interface ${entity_name} : <#if class_parent??>${class_parent}<#else>NSObject</#if>

<#if ( class_properties?size > 0)><#t>
<#list class_properties as property>
${property.property_literal};
</#list><#t>
</#if>

- (RKObjectMapping*)requestMapping;
+ (RKDynamicMapping*)dynamicRequestMapping;
+ (RKObjectMapping*)responseMapping;
+ (RKMapping*)dynamicResponseMapping;
+ (NSString*)typeName;


@end

#endif
