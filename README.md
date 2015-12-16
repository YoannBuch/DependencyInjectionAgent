# Dependency Injection Java Agent

This java agent helps you visualize in your browser the content of the dependency injection container used by your application.

It should support all the dependency injection libraries relying on the @Inject and @Autowired annotations: Spring, CDI, Guice, Dagger, etc.


## How to build the agent

    mvn package

It creates di-agent.jar

## How to use it

Add this argument to the VM used to run your appliation:

    -javaagent:/path/to/di-agent.jar

Once your application is running, you can visualize the content of the containter at:

    http://localhost:9090

## Limitations

This project is just a proof of concept and contains some serious limitations:
- Not all dependencies are injected with @Inject and @Autowired. Some are defined through xml or java configurations.
- Dependencies with special life cycle, usually implemented with proxies, are not properly handled
- The dependency graph keeps a reference on live objects, this is wrong, and should keep a weak reference (like a serialized copy)
- ...
