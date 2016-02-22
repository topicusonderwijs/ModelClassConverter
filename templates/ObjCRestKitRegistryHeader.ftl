//  
//  ${file_name}
//  ${product_name}
//  
//  Automatically generated on ${file_date} at ${file_time}.
//  

#ifndef EntityRegistryHeader
#define EntityRegistryHeader

#import <Foundation/Foundation.h>
#import <RestKit/RestKit.h>

@interface EntityRegistry : NSObject

+ (RKObjectMapping*)mappingFor:(NSString*)type;

@end

#endif
