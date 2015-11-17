Getting Started
===============

Download
--------

To make a scribble, import the maven dependency into your project, preferrably in scope  test

```xml
    <dependency>
        <groupId>io.inkstand</groupId>
        <artifactId>scribble</artifactId>
        <version>0.3.0</version>
        <scope>test</scope>
    </dependency>
```

or download it manually from Maven:

- STABLE: http://central.maven.org/maven2/io/inkstand/scribble/0.2.0/scribble-0.2.0.jar
- LATEST: http://central.maven.org/maven2/io/inkstand/scribble/0.3.0/scribble-0.3.0.jar

First Scribble
--------------

###Content Repository

To write tests for a class that requires direct access to a JSR 283 based content repository, you might not want to set 
the repository up for yourself or to mock all the semantics. Then start your scribble like this

```java
    @Rule
    public ContentRepository repository = Scribble.newInMemoryContentRepository().build();
```

In a complete test, this could look like

_Example Test:_

```java
    import static io.inkstand.scribble.Scribble.*;
    
    public class MyTest {
    
     @Rule
     public ContentRepository repository = newInMemoryContentRepository().build();
    
     @Test
     public void test_withRepository() {
        //given
        javax.jcr.Repository repo = repository.getRepository();
        //when
        ...
     }
    
     @Test
     public void test_withSession() {
        //given
        javax.jcr.Session session = repository.login("user", "password");
        //when
        ...
     }
    }
```

###LDAP Server

If you write a test case that requires an LDAP server and you don't want to rely on an external infrastructure component 
to run your test, you could use the embedded LDAP server provided by Apache DS. Or you could use Scribble, that does the 
job for you. Scribble contains a test rule for an Apache DS based LDAP server. To include the rule, add the following 
line:

```java
    @Rule
    public DirectoryServer ldapServer = Scribble.newDirectoryServer().onPort(389).build;
```

This brings up an LDAP server on port 389 during the before and after phase of the test execution.

To add LDAP entry you can use the Apache DS API on the underlying service, accessible through
DirectoryService directory = ldapServer.getDirectoryService();

You also have the option, to load content from an LDIF File into a partition:

```java
    //create the partition
    directory.addPartition("testPartition", "dc=test");
    directory.importLdif(new FileInputStream("yourDirectoryContent.ldif");
```

The first entry in your LDIF file has to be the context entry specified by the partition DN (i.e. dc=test)

or add the content right from the start in the rule definition

```java
    @Rule
    public DirectoryServer ldapServer = Scribble.newDirectory()
                                                .withPartition("test", "cn=test")
                                                .importLidf(ldifURL)
                                                .aroundDirectoryServer()
                                                .onPort(389)
                                                .build;
```
 
