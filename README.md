# Container

A set of classes capable of holding objects.

## Overview

### ObjectToObjectMap
Given one object, gives another selected based on some criteria.

#### Interface
```java
public interface ObjectToObjectMap<K, V>
{
    public V getValueBy(K keyObject) throws UnableToGetValue;
}
```

#### Implementations

##### ExactClassBasedMap
Associates values with exact classes of objects used to get them. Inheritance **is not** supported.
```java
(new ExactClassBasedMap<Object, String>())
    .associate(Integer.class, "int")
    .associate(String.class, "string")
    .getValueBy(123); //"int"
```

##### UpperBoundedClassBasedMap
Associates values with classes of objects used to get them. Inheritance **is** supported.
```java
(new UpperBoundedClassBasedMap<Object, String>())
    .associate(Number.class, "number")
    .associate(String.class, "string")
    .getValueBy(123); //"number"
```
## ManyObjectsToObjectMap
Given many objects, gives one other object.

#### Interface
```java
public interface ManyObjectsToObjectMap<E>
{
    public E getValueBy(Object... keyObjects) throws UnableToGetValue;
}
```

#### Implementations
##### ManyExactClassesBasedMap
Associates values with lists of exact classes of objects used to get them. Inheritance **is not** supported.

```java
(new ManyExactClassesBasedMap<String>())
    .associate(
        new Class[]{Integer.class, String.class},
        "int-string"
    )
    .associate(
        new Class[]{String.class},
        "string"
    )
    .getValueBy(123, "abc"); //"int-string"

```
##### ManyUpperBoundedClassesBasedMap
Associates values with lists of classes of objects used to get them. Inheritance **is** supported.

```java
(new ManyUpperBoundedClassesBasedMapTest<String>())
    .associate(
        new Class[]{Number.class, String.class},
        "number-string"
    )
    .associate(
        new Class[]{String.class},
        "string"
    )
    .getValueBy(123, "abc"); //"number-string"

```

## More information
Please check unit tests, as they cover all of the details not mentioned here.
