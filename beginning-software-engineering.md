#Beginners Guide to Software Engineering

This is targeted to those who are thinking of becoming software engineers, but don’t have much experience writing code.
I will be focusing on what you really need to know early to accelerate your rate of learning as much as possible.
I also want to help you avoid mistakes and bad habits common in even experienced software engineers.
The most important things to learn first are broken down into 3 categories.

* Critical Thinking
* Simplicity
* Skills

## Critical Thinking

* Critical thinking must be is learned, it is not innate
* Before creating a solution, understand what it means to be a problem
* Logical fallacy of "Appeal to Authority"
* Logical fallacy of "Appeal to Ignorance"
* Confirmation Bias

Software engineering is “thought work”.
Your goal is to solve a customer’s problem, and your tool is your brain;  not code, not computers, your brain.
In fact, the best solutions you come up with will be the ones where no code was written.

So, creative, critical thinking is the name of the game; your first inclination needs to be to think about the problem and question your assumptions.
Don’t assume that the problem is the same as one you’ve encountered before.
Don’t assume the experts know better.

### Critical thinking must be is learned, it is not innate

Critical thinking is a skill that requires discipline and training over a lifetime.
It is not natural and has very little to do with how smart you are.

### Before creating a solution, understand what it means to be a problem

Quote from "Are Your Lights On?", by Donald C. Gause and Gerald M. Weinberg

    A PROBLEM IS A DIFFERENCE
    BETWEEN THINGS AS DESIRED
    AND THINGS AS PERCEIVED

A common mistake is to underrate the importance in perception.
Sometimes you can fix a problem simply by having a sensible error message.
This can shift perception from "the program isn't working" to "oops, I should go back and fix my mistake". 
Other times you can make a web page seem more performant by loading what data you have now, and using spinners to indicate the rest is on its way.

### Logical fallacy of "Appeal to Authority"

When you are new, it can be hard to tell the difference between genuine experts and pontificating blowhards.
Developers that know what they are talking about

* will be able to give you reasons for their claims
* are happy to explain what you don't understand
* admit when they have not personally verified a claim

Developers you cannot learn from

* try to dazzle you with their "experience"
* want you to "trust" that they "know" what they are talking about without explaining why
* point to unrelated personal accomplishments rather than related evidence 

### Logical fallacy of "Appeal to Ignorance"

One common bad habit is to assume we aren't smart enough to solve a problem, so we copy & paste code we don't understand, or use a framework/library we don't understand, figuring the implementers must have known what they were doing.
Sometimes the implementers were solving a different problem, or sometimes they were wrong, so what this habit does is cause bad code to replicate throughout the code base.
It is ok not rely on untested and unproven technologies in production, but to rely on code you don't even understand is even worse.

### Confirmation Bias

We humans tend to see what we expect to see.
Try to decide what would be evidence for and against your guess before you check the facts.

## Simplicity (in priority order)

* Meet Customer Need
* Safe to Change
* Clearly Express Intent
* No Duplicate Code
* Concise as Possible

Simple does not mean easy.
Do not be impressed by how clever code looks.
The best solutions look like they took almost no thinking or effort at all, but this is deceptive.
It takes quite a bit of skill and discipline to ensure a solution is as simple as it can be. 
Another common mistake is to focus on what makes code easier to write rather than easier to maintain.
This is wrong because you only write code once, but you maintain it continually.

### Meet Customer need

This can be well expressed by a slight modification to a quote by Richard Feynman.  The original quote is

    It does not make any difference how beautiful your guess is.
    It does not make any difference how smart you are, who made the guess, or what his name is.
    If it disagrees with experiment it is wrong.
    That is all there is to it.

My paraphrasing to relate this to software engineering is:

    It does not make any difference how beautiful your code is.
    It does not make any difference how smart you are, who wrote the code, or what their name is.
    It does not matter how closely it matches the specification,
    or if it is exactly what the customer asked for.
    If it does not meet the customer need, it is wrong.
    That is all there is to it.

### Safe to Change

If you make a change to the code which introduces unintended consequences, you should get feedback about that as soon as possible.
This means your code will need automated tests.

The best times to get feedback (in priority order)

* Instant (for example, your Integrated Development Environment may be able to detect typos in statically typed languages)
* Compile Time
* Automated Unit Tests (unit tests are fast because they don't touch things you don't control)
* Automated Integration Tests (integration tests verify a single thing you don't control, such as a database)
* Automated Smoke Tests (smoke tests verify all the application parts are configured together correctly)
* Manual Tests
* Customer Feedback

### Clearly Express Intent

You should be able to tell what code does by looking at it.
Code constructs should have names that correspond to what they represent.
A comment in code is a lie waiting to happen.
Design by contract helps with this.

### No Duplicate Code

The refactorings will help you with this.
It is especially important to note that this is lower priority than clearly expressing intent.

### Concise as Possible

Make sure you are not writing anything that does not meet the customer need.
If code is not used anymore, throw it out.
If you need to, you can always get it back with version control, but it is usually better to rewrite it because you will have more information later.

## Skills

* Test Driven Design
* Design by Contract
* Extract Method
* Replace Conditional with Polymorphism
* Don’t Optimize Early

### Test Driven Design

If you write the code before you write the test you are coding backwards.
First, start with a test that fails because your code does not implement the feature.
By doing this, you are unambiguously defining what the code should do.
Once you know with precision what the code should do, you are equipped to write it.
As you make design mistakes the tests will become hard to write and maintain.
This is a good thing.
It warns you that it is time to stop and think about your design before creating more code that is badly designed.

### Design by Contract

The contract must be explicitly clear in the code.
For pure functions, the sole source of input is the signature, and the sole source of output is the return value.
There are no side effects, mutating state, or hidden inputs.
For side-effecting functions, the nature of the side effect is clearly expressed in the name.
Side-effecting functions should have no logic, and only do what their name implies.
For objects, all collaborators and configuration are sent via the constructor function, and all capabilities are encapsulated in the created object.
Implementation details of the created object are hidden behind the proper programming language abstraction (such as an interface or protocol)

### Extract Method (from the book "Refactoring")

(in this context, the difference between "function" and "method" is not relevant)
Functions allow you to give a human readable name to a block of code, making it easier to understand.
It is perfectly fine to have all of your functions be only 2-4 lines of code.
Functions can also be used to refactor some types of duplication.
When possible, your functions should be referentially transparent.

### Replace Conditional with Polymorphism (from the book "Refactoring")

See "Refactoring" by Martin Fowler and practice on your own to master this.
It takes a while to figure out, but is one of your most important tools.
If you get good at this, you will automatically get good at object oriented design over time.
In fact, the object oriented designs you naturally evolve will be better than planned designs, as they will be based on what you actually needed rather than what you thought you might need.

### Don’t Optimize Early

Instead focus on simple, clean, easy to understand code.
Humans are terrible at predicting which code will be the real problem.
Optimizations will also never do better than designing away the need to invoke any code at all.
If there is a problem that seems to require optimization, add metrics to verify where the problem really is.
Make sure the customer really needs the problem to be solved, sometimes an alternative design or flow will make the optimization unnecessary.
If the customer actually needs the code to go faster, this becomes part of the requirements, and is treated accordingly.
In this sense, the section could have been titled "Don't optimize at all", because performance is already covered under "Meet Customer Need".
