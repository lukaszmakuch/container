package pl.lukaszmakuch.container.ManyObjectsToObject;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import pl.lukaszmakuch.container.Exception.UnableToGetValue;

public class ManyUpperBoundedClassesBasedMap<E> implements ManyObjectsToObjectMap<E>
{
    private Deque<List<Class>> recentlyRegisteredListsOfClasses = new ArrayDeque<>();
    private Map<List<Class>, E> valuesAssignedToListsOfClasses = new HashMap<>();
    
    public ManyUpperBoundedClassesBasedMap<E> associate(
        Class[] keyObjectClasses,
        E value
    ) {
        recentlyRegisteredListsOfClasses.push(Arrays.asList(keyObjectClasses));
        valuesAssignedToListsOfClasses.put(
            Arrays.asList(keyObjectClasses), 
            value
        );
        return this;
    }
    
    @Override
    public E getValueBy(Object... keyObjects) throws UnableToGetValue
    {
        List<Class> classes = getFirstListOfRegisteredClassesMatching(keyObjects);
        E foundValue = valuesAssignedToListsOfClasses.get(classes);
        if (null == foundValue) {
            throw new UnableToGetValue("unsupported list of key objects");
        }
        
        return foundValue;
    }
    
    private List<Class> getFirstListOfRegisteredClassesMatching(Object[] keyObjects)
    {
        for (List<Class> classes: recentlyRegisteredListsOfClasses) {
            if (match(keyObjects, classes)) {
                return classes;
            }
        }
        
        return null;
    }
    
    private boolean match(Object[] keyObjects, List<Class> classes)
    {
        if (keyObjects.length != classes.size()) {
            return false;
        }
        
        for (int i = 0; i < keyObjects.length; i++) {
            if (!classes.get(i).isInstance(keyObjects[i])) {
                return false;
            }
        }
        
        return true;
    }
}
