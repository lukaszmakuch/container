package pl.lukaszmakuch.container;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import pl.lukaszmakuch.container.Exception.UnableToGetValue;

public class ManyUpperBoundedClassesBasedMap<E> implements ManyObjectsToObjectMap<E>
{
    private Deque<List<Class>> supportedClasses = new ArrayDeque<>();
    private Map<List<Class>, E> valuesByClasses = new HashMap<>();
    
    public ManyUpperBoundedClassesBasedMap<E> associate(
        Class[] keyObjectClasses,
        E value
    ) {
        supportedClasses.push(Arrays.asList(keyObjectClasses));
        valuesByClasses.put(Arrays.asList(keyObjectClasses), value);
        return this;
    }
    
    
    @Override
    public E getValueBy(Object... keyObjects) throws UnableToGetValue
    {
        List<Class> firstListOfSupportingClasses = getFirstListOfSupportingClasses(
            supportedClasses,
            keyObjects,
            0
        );
        
        E foundValue = valuesByClasses.get(firstListOfSupportingClasses);
        if (null == foundValue) {
            throw new UnableToGetValue("unsupported list of key objects");
        }
        
        return foundValue;
    }
    
    private List<Class> getFirstListOfSupportingClasses(
        Deque<List<Class>> listsOfSupportedClasses,
        Object[] keyObjects,
        int currentKeyObjectIndex
    ) {
        Deque<List<Class>> remainingListsOfSupportedClasses = new ArrayDeque<>();
        for (List<Class> classes: listsOfSupportedClasses) {
            if (
                (currentKeyObjectIndex < classes.size())
                && (classes.get(currentKeyObjectIndex).isInstance(keyObjects[currentKeyObjectIndex]))
                && (classes.size() == keyObjects.length)
            ) {
                if (currentKeyObjectIndex == (keyObjects.length - 1)) {
                    return classes;
                }
                
                remainingListsOfSupportedClasses.add(classes);
            }
        }
        
        if (currentKeyObjectIndex >= (keyObjects.length - 1)) {
            return null;
        }
        
        return getFirstListOfSupportingClasses(
            remainingListsOfSupportedClasses,
            keyObjects,
            currentKeyObjectIndex + 1
        );
    }
    
}
