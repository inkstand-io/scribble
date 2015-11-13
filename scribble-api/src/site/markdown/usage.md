Usage
=====

io.inkstand.scribble.rules.BaseRule
-----------------------------------

The io.inkstand.scribble.rules.BaseRule is the base class for all the Scribble rules. It implements the TestRule 
interface of Junit and contains the chaining mechanism. It provides access to the outer rule and the wrapped execution 
thereof. Further provides the capability of tracking the initialization of the rule to support "late" initialization 
in a test's @Before methods (and vice versa for tear-down).

Usually you don't have to deal with the BaseRule directly.

io.inkstand.scribble.rules.ExternalResource
-------------------------------------------

The io.inkstand.scribble.rules.ExternalResource is an alternative to the ExternalResource provided by JUnit. The 
ExternalResource provided by JUnit provides a default Statement wrapped around the base statement that invokes a 
overridable before and after method. The rule does not differentiate if it is a @ClassRule or a @Rule. But there are 
some occasions where the setup and teardown behavior is different when being used as class rule instead of test rule.

The external resource has a detection of the context and invokes different setup/teardown methods when being used as 
@ClassRule. Therefore it provides the empty-bodied, overridable methods

- beforeClass
- before
- after
- afterClass

As the methods are independent of each other, it is even possible to use the same rule instance as @ClassRule AND @Rule
public MyTest {
 
    @ClassRule
    public static ExternalResource scribbleResource = ...
     
    @Rule
    public ExternalResource testResource = scribbleResource;
     
    ...
    }

A typical use case scenario could be

1. beforeClass: startServer
2. before: prepareServerContent
3. do your first test
4. after: destroyServerContent
5. before: prepareServerContent
6. do your second test
7. after: destroyServerContent
8. afterClass: stopServer

This allows to optimize your test-execution time by doing time-consuming setup/teardown only once, as long as the 
potentially destructive behavior or your tests allows for doing so.

Finally, as the ExternalResource inherits from the BaseRule it supports rule chaining.
