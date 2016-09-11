package pl.lukaszmakuch.container.ObjectToObject;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import pl.lukaszmakuch.container.Exception.UnableToGetValue;

public class UpperBoundedClassBasedMap<K, V> implements ObjectToObjectMap<K, V>
{
    private Deque<Class<? extends K>> supportedClasses = new ArrayDeque<>();
    private Map<Class<? extends K>, V> valuesByClasses = new HashMap<>();
    
    public UpperBoundedClassBasedMap<K, V> associate(
        Class<? extends K> keyObjectClass, 
        V value
    ) {
        valuesByClasses.put(keyObjectClass, value);
        supportedClasses.push(keyObjectClass);
        return this;
    }
    
    @Override
    public V getValueBy(K keyObject) throws UnableToGetValue
    {
        for(Class oneOfPossibleClasses: supportedClasses) {
            if (oneOfPossibleClasses.isInstance(keyObject)) {
                return valuesByClasses.get(oneOfPossibleClasses);
            }
        }
        
        throw new UnableToGetValue(String.format(
            "%s isn't an instance of any of the supported classes of keys",
            keyObject.getClass().getCanonicalName()
        ));
    }
}
