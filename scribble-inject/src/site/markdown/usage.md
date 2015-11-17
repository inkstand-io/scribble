Usage
=====

Using the annotation based mocking feature of Mockito you can easily inject mock instances into a class under test

    @RunWith(MockitoJUnitRunner.class)
    public class MyTest {
     
    @Mock
    private SomeType mock;
     
    @InjectMocks
    private ClassUnderTest subject;
     
    ...

It's a neat a simple way of populating your testee without much code and is totally sufficient for most cases, but the 
mechanism has some limitations

- You can only inject mocks or spies. Classes that are not mockable/spyable (final classes like String) can not be 
injected
- Having multiple mocks of the same type gives you no guarantee, which instance is injected (usually the first, but 
it's not guaranteed)
- no support of any CDI qualifiers 

Of course there are alternatives to that, i.e. using the Weld SE CDI container to put your class under test into and 
resolve its dependencies. This option would be definitely conform to standards but would also require that you actually 
satisfy every dependency in the type structure. Further this would bloat an otherwise simple test setup.

Scribble Injection support
--------------------------
Scribble provides a declarative injection mechanism that allows to inject instances into specific fields.

###Basic Injection mechanism

The simplest form of injection is

    Scribble.inject(value).into(target);

Which will inject the value into the first type compatible field of the target. The fields can be of any visibility and 
within the type hierarchy of the target. The result is basically the same as with Mockito.

To inject the value into all type compatible fields of the target you call

    Scribble.inject(value).intoAll(target);

You can instantiate the Injection directly, too. Using new io.inkstand.scribble.inject.Injection(value). 
But it's recommended to use the factory method of Scribble.

Resource Injection
------------------

Assume you have a class under test, that has one (or more) field that is annotated with ```javax.annotation.Resource``` 
like

    public class ResourceConsumer {
     
    @Resource(name="sampleResource")
    private MyService serviceA;
     
    @Resource(lookup="java:/sample/resource")
    private MyService serviceB

In a EE container the resources are injected according to the attributes of the @Resource annotation. With Scribble you 
can do the same and inject service (mocks) into the specific fields.

    @Mock MyService serviceA;
    @Mock MyService serviceB;
    public ResourceConsumer testee;
     
    @Before
    public void setUp() {
        Scribble.inject(serviceA).asResource().byName("sampleResource").into(testee);
        Scribble.inject(serviceA).asResource().byLookup("java:/sample/resource").into(testee);
    }

The Resource-injection mechanism supports to specify the target resource by

- name
- mappedName
- lookup

Which can be combined i.e. name AND lookup, which both have to match.

    Scribble.inject(serviceA).asResource().byName("sampleResource").byLookup("java:/sample/resource").into(testee);

The @Resource injection mechanism depends to a certain degree on the knowledge of the internal structure of the 
testee, like the lookup name. But as this is considered to be part of the testee's type contract, it's a lesser form of 
whitebox testing than knowing the actual name of field. Further you have to know the @Resource attributes as well 
for setting up the real container before deploying the testee.

###Deltaspike ConfigurationProperty

The Apache Deltaspike Project provides a set of portable CDI extensions. Part of the Core API is a configuration 
mechanism, providing mechanism for configuring your application using CDI. The injection points into which 
configuration entities should be injected are annotated using the @ConfigProperty qualifier annotation. While DeltaSpike 
provides various ways of satisfying the @ConfigProperty dependencies in a real CDI container, Scribble provides the 
injection mechanism to satisfy the dependency in a unit test.

The @ConfigProperty has a name attribute and optionally a default value.

    @Inject
    @ConfigProperty(name = "my.example.property")
    private String exampleProperty;
     
    @Inject
    @ConfigProperty(name = "my.default.property", defaultValue = "aValue")
    private String exampleProperty;

To inject values in the property fields, make the following Scribble

    Scribble.inject(value).asConfigProperty(configPropertyName).into(testee);

The value can be of any type but it has to be compatible with the type of the target field. The Scribble for the 
@ConfigProperty examples above would be

    Scribble.inject("sampleString").asConfigProperty("my.example.property").into(testee);

In order to inject the default value of the @ConfigProperty the value of the injection itself has to be null.

    Scribble.inject(null).asConfigProperty("my.default.property").into(testee);

The injection mechanism does support to inject primitive values and their wrapper pendants into primitive field as well 
as converting a string value into any primitive field, as long as the string has the correct format.