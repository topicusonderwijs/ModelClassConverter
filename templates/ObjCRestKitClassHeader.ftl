//  
//  ${file_name}
//  ${entity_identifier}
//  ${product_name} (${model_version})
//  
//  Automatically generated on ${file_date} at ${file_time} by ${user}.
//  

#ifndef ${entity_name}Header
#define ${entity_name}Header

#import <Foundation/Foundation.h>
#import <RestKit/RestKit.h>
#import "EntityRegistry.h"
<#if class_parent??>#import "${class_parent_literal}.h"</#if>
<#list class_imports as import>
#import "${import}.h"
</#list>
<#if ( class_properties?size > 0)><#t>
<#list class_constants as constant>
#define ${entity_name}${constant.property_literal}
</#list>

</#if>
@interface ${entity_name} : <#if class_parent_literal??>${class_parent_literal}<#else>NSObject</#if>

<#if ( class_properties?size > 0)><#t>
<#list class_properties as property>
${property.property_literal};
</#list><#t>
</#if>

- (RKObjectMapping*)requestMapping;
+ (RKMapping*)dynamicRequestMapping;
+ (RKObjectMapping*)responseMapping;
+ (RKMapping*)dynamicResponseMapping;
+ (NSString*)descriptor;


@end

#endif
