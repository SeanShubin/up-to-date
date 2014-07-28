Up to Date
=
Will help keep your maven project dependencies up to date

This is a work in progress.  This readme will be updated when it is in a functional state.

Currently you will not be able to compile the console module without setting up [dependency analyzer](https://github.com/SeanShubin/dependency-analyzer) and [dot](http://graphviz.org) first.

If dependency analysis is not important to you, you can just disable the exec-maven-plugin entry in the console module

Design by Contract Presentation
=
- Assumed values
    - Maintainable code
    - Easy to change
    - Good test coverage
    - Easy to test
- Design By Contract
    - Object-Oriented Software Construction - Bertrand Meyer
        - Pre-conditions
        - Post-conditions
        - Invariants
    - Contract of a function
        - input -> function -> output
    - Contract of constructor injection
        - collaborators -> constructor -> instance
    - Expressiveness of contract
        - Static Typing
        - Protocols        
- Should x be behind a contract?
    - Sometimes this is a judgment call, sometimes there is a correct answer
    - If the code is more comprehensible as a unit, it should not be split into contracts
    - If the code is talking to something you do not control, it should be placed behind a contract, for example
        - system clock
            - if the system clock is only accessed once, pass an argument instead
            - if the system clock is accessed at different times, you need a contract
        - environment variable
            - if you are not changing them while running, pass them in as parameters
        - network
            -http
        - local time zone
        - random
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
        - [Http](logic/src/main/scala/com/seanshubin/up_to_date/logic/Http.scala)
        - [HttpCache](logic/src/main/scala/com/seanshubin/up_to_date/logic/HttpCache.scala)
        - [HttpCacheTest](logic/src/test/scala/com/seanshubin/up_to_date/logic/HttpCacheTest.scala)
    - Integration Testing
        - [FileSystem](logic/src/main/scala/com/seanshubin/up_to_date/logic/FileSystem.scala)
        - [FileSystemImpl](integration/src/main/scala/com/seanshubin/up_to_date/integration/FileSystemImpl.scala)
        - [FileSystemTest](integration/src/test/scala/com/seanshubin/up_to_date/integration/FileSystemTest.scala)
        - [Http](logic/src/main/scala/com/seanshubin/up_to_date/logic/Http.scala)
        - [HttpImpl](integration/src/main/scala/com/seanshubin/up_to_date/integration/HttpImpl.scala)
        - [HttpTest](integration/src/test/scala/com/seanshubin/up_to_date/integration/HttpTest.scala)
    - Wiring
        - [ProductionRunnerWiring](console/src/main/scala/com/seanshubin/up_to_date/console/ProductionRunnerWiring.scala)
        - Statically typed
        - Compiled
            - no scanning xml files at run time
            - no scanning .class files at run time
        - Breakpoints
        - No proxies in stack trace
        - Can follow the code in IDE
    - [Logging as a first class citizen](http://blog.cj.com/05212013/logging-first-class-citizen)
- Constructor injection vs setter injection
    - Why create it the wrong way and mutate it to be correct when you can create it correctly in the first place?
    - If you have static typing and a compiler, you never forget to add a collaborator
- What if you don't have static typing or protocols?
    - You still benefit from design by contract, but you don't have any support until runtime
    - This is our problem in javascript
    - You can try to make the runtime support more effective, as with [protocop](https://github.com/cjdev/protocop)
    - How much do we value maintainability?  Statically typed languages that compile to javascript could be worth a try
        - [Dart](https://www.dartlang.org)
        - [TypeScript](http://www.typescriptlang.org)
