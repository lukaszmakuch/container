package pl.lukaszmakuch.container;

import pl.lukaszmakuch.container.Exception.UnableToGetValue;

public interface ManyObjectsToObjectMap<E>
{
    public E getValueBy(Object... keyObjects) throws UnableToGetValue;
}
