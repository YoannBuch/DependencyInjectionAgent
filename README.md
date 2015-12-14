# Dependency Injection Java Agent

This java agent helps you visualize in your browser the content of the dependency injection container used by your application.


## How to build the agent

    mvn package

It creates di-agent.jar

## How to use it

Add this argument to the VM used to run your appliation:

    -javaagent:/path/to/di-agent.jar

Once your application is running, you can visualize the content of the containter at:

    http://localhost:9090
