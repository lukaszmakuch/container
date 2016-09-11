package pl.lukaszmakuch.container.ManyObjectsToObject;

import pl.lukaszmakuch.container.ManyObjectsToObject.ManyExactClassesBasedMap;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import pl.lukaszmakuch.container.Exception.UnableToGetValue;

public class ManyExactClassesBasedMapTest
{
    @Rule
    public ExpectedException thrown = ExpectedException.none();
    
    private interface Key {};
    private class FirstKey implements Key {}
    private class FirstKeyChild extends FirstKey {}
    private class SecondKey implements Key {}
    
    private ManyExactClassesBasedMap<String> map = new ManyExactClassesBasedMap<>();
    
    @Test
    public void correctFlow() throws UnableToGetValue
    {
        map.associate(
            new Class[]{FirstKey.class},
            "just the first key"
        ).associate(
            new Class[]{FirstKey.class, SecondKey.class},
            "many keys"
        ).associate(
            new Class[]{String.class, Integer.class},
            "some other types"
        );
        
        assertEquals(
            "just the first key", 
            map.getValueBy(new FirstKey())
        );
        assertEquals(
            "many keys", 
            map.getValueBy(new FirstKey(), new SecondKey())
        );
        assertEquals(
            "some other types", 
            map.getValueBy("abc", 123)
        );
    }
    
    @Test
    public void overriding() throws UnableToGetValue
    {
        map.associate(
            new Class[]{String.class, Integer.class},
            "value that's going to be overriden"
        ).associate(
            new Class[]{String.class, Integer.class},
            "overriden value"
        );
        
        assertEquals(
            "overriden value", 
            map.getValueBy("abc", 123)
        );
    }
    
    @Test
    public void exceptionWhenUnsupportedKey() throws UnableToGetValue
    {
        map.associate(
            new Class[]{FirstKey.class, String.class},
            "the only supported set of classes"
        );
        
        thrown.expect(UnableToGetValue.class);
        
        map.getValueBy(new FirstKey(), 123);
    }
    
    @Test
    public void exceptionWhenNotExactlyTheSameClass() throws UnableToGetValue
    {
        map.associate(
            new Class[]{FirstKey.class, String.class},
            "inheritance is not supported"
        );
        
        thrown.expect(UnableToGetValue.class);
        
        map.getValueBy(new FirstKeyChild(), "abc");
    }
    
}
