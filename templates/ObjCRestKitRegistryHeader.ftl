//  
//  ${file_name}
//  ${product_name}
//  
//  Automatically generated on ${file_date} at ${file_time} by ${user}.
//  

#ifndef EntityRegistryHeader
#define EntityRegistryHeader

#import <Foundation/Foundation.h>
#import <RestKit/RestKit.h>

@protocol EntityRegistryDelegate <NSObject>

- (RKMapping*)responseMappingFor:(Class)c;
- (RKMapping*)requestMappingFor:(Class)c;
- (id)initializeEntity:(id)entity;

@end

@interface EntityRegistry : NSObject

+ (void)setDelegate:(id<EntityRegistryDelegate>)registryDelegate;

+ (Class)classForDescriptor:(NSString*)descriptor;

+ (RKMapping*)responseMappingFor:(Class)c;
+ (RKMapping*)requestMappingFor:(Class)c;
+ (id)initializeEntity:(id)entity;

@end

#endif
