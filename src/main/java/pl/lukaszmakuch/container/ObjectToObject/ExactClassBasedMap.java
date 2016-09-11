package pl.lukaszmakuch.container.ObjectToObject;

import pl.lukaszmakuch.container.Exception.UnableToGetValue;
import java.util.Map;
import java.util.HashMap;

public class ExactClassBasedMap<K, V> implements ObjectToObjectMap<K, V>
{
    private Map<Class, V> valuesByClasses = new HashMap<>();
    
    public ExactClassBasedMap<K, V> associate(
        Class<? extends K> keyObjectClass, 
        V value
    ) {
        valuesByClasses.put(keyObjectClass, value);
        return this;
    }
    
    @Override
    public V getValueBy(K keyObject) throws UnableToGetValue
    {
        V foundValue = valuesByClasses.get(keyObject.getClass());
        if (null == foundValue) {
            throw new UnableToGetValue(String.format(
                "there's no value associated with the %s class",
                keyObject.getClass().getCanonicalName()
            ));
        }

        return foundValue;
    }
}
