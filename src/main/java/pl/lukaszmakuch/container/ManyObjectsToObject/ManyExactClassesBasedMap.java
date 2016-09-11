package pl.lukaszmakuch.container.ManyObjectsToObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import pl.lukaszmakuch.container.Exception.UnableToGetValue;

public class ManyExactClassesBasedMap<E> implements ManyObjectsToObjectMap<E>
{
    private Map<List<Class>, E> valuesByClasses = new HashMap<>();
    
    public ManyExactClassesBasedMap<E> associate(
        Class[] keyObjectClasses,
        E value
    ) {
        valuesByClasses.put(Arrays.asList(keyObjectClasses), value);
        return this;
    }
    
    @Override
    public E getValueBy(Object... keyObjects) throws UnableToGetValue
    {
        List<Class> keyObjectsClasses = new ArrayList<>();
        for (Object o: keyObjects) {
            keyObjectsClasses.add(o.getClass());
        }

        E foundValue = valuesByClasses.get(keyObjectsClasses);
        if (null == foundValue) {
            throw new UnableToGetValue("there's no value associated with the given list of key objects");
        }
        
        return foundValue;
    }
}
