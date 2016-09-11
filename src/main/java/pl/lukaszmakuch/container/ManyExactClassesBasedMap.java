package pl.lukaszmakuch.container;

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
        //because it should work with java 7 and it makes no sense to include
        //a libary for that single method
        List<Class> keyObjectsClasses = new ArrayList<>();
        for (Object o: keyObjects) {
            keyObjectsClasses.add(o.getClass());
        }

        E foundValue = valuesByClasses.get(keyObjectsClasses);
        if (null == foundValue) {
            String listOfUnsupportedClasses = "";
            for (Class c: keyObjectsClasses) {
                if (!listOfUnsupportedClasses.isEmpty()) {
                    listOfUnsupportedClasses += ", ";
                }
                
                listOfUnsupportedClasses += c.getCanonicalName();
            }
            throw new UnableToGetValue(String.format(
                "there's no value associated with the following list of classes: %s",
                listOfUnsupportedClasses
            ));
        }
        return foundValue;
    }
}
