Up to Date
=
Will help keep your maven project dependencies up to date

This is a work in progress.  This readme will be updated when it is in a functional state.

Currently you will not be able to compile the console module without setting up [dependency analyzer](https://github.com/SeanShubin/dependency-analyzer) and [dot](http://graphviz.org) first.

If dependency analysis is not important to you, you can just disable the exec-maven-plugin entry in the console module

Design by Contract Presentation
=
- Maintainable Code
- Easy to Test
- Design By Contract
    - Object-Oriented Software Construction - Bertrand Meyer
        - Pre-conditions
        - Post-conditions
        - Invariants
    - Contract of a function
        - input -> function -> output
    - Contract of constructor injection
        - collaborators -> constructor -> instance
- Should x be behind a contract?
    - Sometimes this is a judgment call, sometimes there is a correct answer
    - If the code is more comprehensible as a unit, it should not be split into contracts
    - If the code is talking to something you do not control, it should be placed behind a contract, for example
        - system clock
            - if the system clock is only accessed once, pass an argument instead
            - if the system clock is accessed at different times, you need a contract
        - environment variable
            - if you are not changing them while running, pass them in as parameters
        - local time zone
        - random
        - network
        - filesystem
        - database
        - implementation details unrelated to need
- Bad examples
    - Example of system clock
    - Example of environment variable
    - Example of implemented details unrelated to need
        - Expose the syntax of our search to end customers
        - Now we have to emulate that syntax if we change our implementation
- Good examples
    - Logic Testing
        - [Http](blob/master/logic/src/main/scala/com/seanshubin/up_to_date/logic/Http.scala)
        - [HttpCache](blob/master/logic/src/main/scala/com/seanshubin/up_to_date/logic/HttpCache.scala)
        - [HttpCacheTest](blob/master/logic/src/main/scala/com/seanshubin/up_to_date/logic/HttpCacheTest.scala)
    - Integration Testing
        - [FileSystem](blob/master/logic/src/main/scala/com/seanshubin/up_to_date/logic/FileSystem.scala)
        - [FileSystemImpl](blob/master/integration/src/main/scala/com/seanshubin/up_to_date/integration/FileSystemImpl.scala)
        - [FileSystemTest](blob/master/integration/src/main/scala/com/seanshubin/up_to_date/integration/FileSystemTest.scala)
    - Wiring
        - [ProductionRunnerWiring](blob/master/console/src/main/scala/com/seanshubin/up_to_date/console/ProductionRunnerWiring.scala)
        - Statically typed
        - Compiled
            - no scanning xml files at run time
            - no scanning .class files at run time
        - Breakpoints
        - No proxies in stack trace
        - Can follow the code in IDE
    - [Logging as a first class citizen](http://blog.cj.com/05212013/logging-first-class-citizen)
