# ModelClassConverter

ModelClassConverter analyses your entities in Java and creates an internal model. A generator component then converts this internal model to classes in any language and framework you want. Several generators are provided, but you can define your own by subclassing the base generator and creating your own templates. An elaborate configuration allows you to alter several aspects of the model and the way your entities are generated.

## Generators

Currently available generators are listed below.
 * JavaGenerator (Generic Java generator)
 * ObjCRestKitGenerator (Objective-C generator for RestKit)
 * SwiftGenerator (Generic Swift generator)
 	* SwiftObjectMapperGenerator (Swift generator for ObjectMapper)
 

## Todo

 * Conversion of packages without Maven dependencies
 * Further configuration for custom mapping
 * Subclassing of MCTypes
 * Configuration using MCType and subclasses instead CustomType
 * Resolving class names as well instead of only full identifiers
 * Possible custom native type mapping
 * Support for all properties of enums
 * Support for automatic resolving of imports in Java
 * Support for analysing getters and setters
 * Better support for protocols
 * Further documentation
 
 