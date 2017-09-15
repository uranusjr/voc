package org.python.internals;

public class CollectionOperation {

    public static java.util.Iterator<org.python.Object> getIterator(org.python.Object x) {
        try {
            return ((org.python.types.Iterator) x.__iter__()).getIterator();
        } catch (org.python.exceptions.AttributeError e) {
            throw new org.python.exceptions.TypeError(String.format(
                "'%s' object is not iterable", x.typeName()
            ));
        }
    }

    public static void addAll(org.python.java.Collection target, org.python.Object source) {
        java.util.Collection<org.python.Object> collection = target.getCollection();
        // Optimization if source is backed by a Java collection.
        if (source instanceof org.python.java.Collection) {
            collection.addAll(((org.python.java.Collection) source).getCollection());
            return;
        }
        // Standard implementation using the iterator implementation.
        java.util.Iterator<org.python.Object> iterator = getIterator(source);
        while (iterator.hasNext()) {
            collection.add(iterator.next());
        }
    }
}
