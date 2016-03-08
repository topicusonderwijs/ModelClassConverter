# ModelClassConverter

Usually model classes or entities are defined in the source of a backend. A neat backend will even have several versions of the same model. Converting all of these entities into classes that your client applications can use can be a pain.

That is exactly what this tool does. ModelClassConverter analyses your entities in Java and creates an internal model. A generator component then converts this internal model to classes in any language and framework you want. Several generators are provided, but you can define your own by subclassing the generator and creating your own templates. An elaborate configuration allows you to alter several aspects of the model and the way your entities are generated.
