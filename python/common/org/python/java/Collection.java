package org.python.java;

public interface Collection extends org.python.Object {
    /**
     * Access the Java implementation of the collection.
     *
     * This is useful to optimize things like List.extend to not rely on
     * Python's iterable protocol, but use the underlying java.util.Collection
     * API instead.
     */
    public java.util.Collection<org.python.Object> getCollection();
}
