package pl.lukaszmakuch.container;

import pl.lukaszmakuch.container.Exception.UnableToGetValue;

public interface ObjectToObjectMap<K, V>
{
    public V getValueBy(K keyObject) throws UnableToGetValue;
}
