package org.python.internals;

public class CollectionOperation {

    public static java.util.Iterator<org.python.Object> getIterator(org.python.Object x) {
        try {
            org.python.Object it = x.__iter__();
            if (it instanceof org.python.types.Iterator) {
                return ((org.python.types.Iterator) it).getIterator();
            }
        } catch (org.python.exceptions.AttributeError e) {
        }
        throw new org.python.exceptions.TypeError(String.format(
                "'%s' object is not iterable", x.typeName()
        ));
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

    public static java.lang.String formatCommaSeperatedRepr(org.python.java.Collection x) {
        java.lang.StringBuilder buffer = new java.lang.StringBuilder();
        java.util.Iterator<org.python.Object> iterator = x.getCollection().iterator();
        if (iterator.hasNext()) {
            buffer.append(iterator.next().__repr__());
        }
        while (iterator.hasNext()) {
            buffer.append(", ");
            buffer.append(iterator.next().__repr__());
        }
        return buffer.toString();
    }
}
