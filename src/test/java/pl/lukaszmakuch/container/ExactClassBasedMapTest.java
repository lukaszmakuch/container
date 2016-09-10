package pl.lukaszmakuch.container;

import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import pl.lukaszmakuch.container.Exception.UnableToGetValue;

public class ExactClassBasedMapTest
{
    @Rule
    public ExpectedException thrown = ExpectedException.none();
    
    private interface Key {};
    private class FirstKey implements Key {}
    private class FirstKeyChild extends FirstKey {}
    private class SecondKey implements Key {}
    
    private ExactClassBasedMap<Key, String> map = new ExactClassBasedMap<>();
    
    @Test
    public void correctFlow() throws UnableToGetValue
    {
        map.associate(
            FirstKey.class, 
            "value associated with objects of the 1st class"
        ).associate(
            SecondKey.class,
            "value associated with objects of the 2nd class"
        );
        
        assertEquals(
            "value associated with objects of the 1st class", 
            map.getValueBy(new FirstKey())
        );
        assertEquals(
            "value associated with objects of the 2nd class", 
            map.getValueBy(new SecondKey())
        );
        assertEquals(
            "value associated with objects of the 1st class", 
            map.getValueBy(new FirstKey())
        );
    }
    
    @Test
    public void overriding() throws UnableToGetValue
    {
        map.associate(
            FirstKey.class, 
            "value that's going to be overriden"
        ).associate(
            FirstKey.class, 
            "overriden value"
        );
        
        assertEquals(
            "overriden value", 
            map.getValueBy(new FirstKey())
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
    public void exceptionWhenNotExactlyTheSameClass() throws UnableToGetValue
    {
        map.associate(
            FirstKey.class, 
            "inheritance is not supported"
        );
        
        thrown.expect(UnableToGetValue.class);
        
        map.getValueBy(new FirstKeyChild());
    }
}
