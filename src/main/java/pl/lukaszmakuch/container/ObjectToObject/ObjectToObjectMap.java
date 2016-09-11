package pl.lukaszmakuch.container.ObjectToObject;

import pl.lukaszmakuch.container.Exception.UnableToGetValue;

public interface ObjectToObjectMap<K, V>
{
    public V getValueBy(K keyObject) throws UnableToGetValue;
}
