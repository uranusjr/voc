package org.python.internals;

public class CollectionOperation {

    public static org.python.Object getIter(org.python.Object x) {
        try {
            return x.__iter__();
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
        // Standard implementation using the iterator protocol.
        org.python.Object it = getIter(source);
        while (true) {
            try {
                collection.add(it.__next__());
            } catch (org.python.exceptions.StopIteration si) {
                return;
            }
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
