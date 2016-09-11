package pl.lukaszmakuch.container;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import pl.lukaszmakuch.container.Exception.UnableToGetValue;

public class ManyUpperBoundedClassesBasedMapTest
{
    @Rule
    public ExpectedException thrown = ExpectedException.none();
    
    private interface Key {};
    private class FirstKey implements Key {}
    private class FirstKeyChild extends FirstKey {}
    private class SecondKey implements Key {}
    
    private ManyUpperBoundedClassesBasedMap<String> map = new ManyUpperBoundedClassesBasedMap<>();
    
    @Test
    public void corretFlow() throws UnableToGetValue
    {
        map.associate(
            new Class[]{Number.class, FirstKey.class},
            "value for: Number, FirstKey"
        ).associate(
            new Class[]{Number.class},
            "value for: Number"
        ).associate(
            new Class[]{Number.class, SecondKey.class},
            "value for: Number, SecondKey"
        );
        
        assertEquals(
            "value for: Number, FirstKey",
            map.getValueBy(123, new FirstKey())
        );
        assertEquals(
            "value for: Number, FirstKey",
            map.getValueBy(456.789, new FirstKeyChild())
        );
        assertEquals(
            "value for: Number, SecondKey",
            map.getValueBy(123, new SecondKey())
        );
        assertEquals(
            "value for: Number",
            map.getValueBy(123)
        );
    }
    
    @Test
    public void exceptionIfUnsupportedKeys() throws UnableToGetValue
    {
        map.associate(
            new Class[]{FirstKey.class, SecondKey.class},
            "the only supported list of classes"
        );
        
        thrown.expect(UnableToGetValue.class);
        
        map.getValueBy(new FirstKey());
    }
    
    @Test
    public void overridingAndOrdering() throws UnableToGetValue
    {
        map.associate(
            new Class[]{FirstKey.class, SecondKey.class},
            "value that's going to be overriden"
        );
        assertEquals(
            "value that's going to be overriden", 
            map.getValueBy(new FirstKeyChild(), new SecondKey())
        );
        
        map.associate(
            new Class[]{FirstKey.class, SecondKey.class},
            "overriden value"
        );
        assertEquals(
            "overriden value", 
            map.getValueBy(new FirstKeyChild(), new SecondKey())
        );
        
        map.associate(
            new Class[]{FirstKeyChild.class, SecondKey.class},
            "more specific value"
        );
        assertEquals(
            "more specific value", 
            map.getValueBy(new FirstKeyChild(), new SecondKey())
        );
        
        map.associate(
            new Class[]{FirstKey.class, SecondKey.class},
            "generic value overriden again"
        );
        assertEquals(
            "generic value overriden again",
            map.getValueBy(new FirstKeyChild(), new SecondKey())
        );
    }
}
