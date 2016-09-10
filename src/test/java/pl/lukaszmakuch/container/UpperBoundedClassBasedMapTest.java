package pl.lukaszmakuch.container;

import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import pl.lukaszmakuch.container.Exception.UnableToGetValue;

public class UpperBoundedClassBasedMapTest
{
    @Rule
    public ExpectedException thrown = ExpectedException.none();
    
    private interface Key {};
    private class FirstKey implements Key {}
    private class FirstKeyChild extends FirstKey {}
    private class SecondKey implements Key {}
    
    private UpperBoundedClassBasedMap<Key, String> map = new UpperBoundedClassBasedMap<>();

    @Test
    public void correctFlow() throws UnableToGetValue
    {
        map.associate(
            FirstKey.class, 
            "value associated with instances of the FirstKey class"
        ).associate(
            SecondKey.class,
            "value associated with instances of the SecondClass class"
        );
        
        assertEquals(
            "value associated with instances of the FirstKey class",
            map.getValueBy(new FirstKey())
        );
        assertEquals(
            "value associated with instances of the FirstKey class",
            map.getValueBy(new FirstKeyChild())
        );
        assertEquals(
            "value associated with instances of the SecondClass class",
            map.getValueBy(new SecondKey())
        );
    }
    
    @Test
    public void exceptionWhenUnsupportedKey() throws UnableToGetValue
    {
        map.associate(
            FirstKey.class, 
            "the only supported class"
        );
        
        thrown.expect(UnableToGetValue.class);
        
        map.getValueBy(new SecondKey());
    }
    
    @Test
    public void overridingAndOrdering() throws UnableToGetValue
    {
        map.associate(
            FirstKey.class, 
            "value that's going to be overriden"
        );
        assertEquals(
            "value that's going to be overriden", 
            map.getValueBy(new FirstKeyChild())
        );
        
        map.associate(
            FirstKey.class, 
            "overriden value"
        );
        assertEquals(
            "overriden value", 
            map.getValueBy(new FirstKeyChild())
        );
        
        map.associate(
            FirstKeyChild.class, 
            "more specific value"
        );
        assertEquals(
            "more specific value", 
            map.getValueBy(new FirstKeyChild())
        );
        
        map.associate(
            FirstKey.class, 
            "generic value overriden again"
        );
        assertEquals(
            "generic value overriden again",
            map.getValueBy(new FirstKeyChild())
        );
    }
}
