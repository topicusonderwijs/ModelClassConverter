//  
//  ${file_name}
//  ${product_name} (${model_version})
//  
//  Automatically generated on ${file_date} at ${file_time} by ${user}.
//  

#ifndef EntityRegistryHeader
#define EntityRegistryHeader

#import <Foundation/Foundation.h>
#import <RestKit/RestKit.h>

@protocol EntityRegistryDelegate <NSObject>

- (BOOL)validateVersion:(NSString*)version;
- (RKMapping*)responseMappingFor:(Class)c;
- (RKMapping*)requestMappingFor:(Class)c;
- (id)initializeEntity:(id)entity;

@end

@interface EntityRegistry : NSObject

+ (NSString*)version;

+ (void)setDelegate:(id<EntityRegistryDelegate>)registryDelegate;
+ (BOOL)hasDelegate;
+ (id<EntityRegistryDelegate>)delegate;

+ (Class)classForDescriptor:(NSString*)descriptor;

+ (RKMapping*)responseMappingFor:(Class)c;
+ (RKMapping*)requestMappingFor:(Class)c;
+ (id)initializeEntity:(id)entity;

@end

#endif
