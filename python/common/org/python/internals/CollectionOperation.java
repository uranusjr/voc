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

    /**
     * Helper to compare the two collections.
     * The second argument MUST conform to org.python.java.Collection.
     */
    private static org.python.Object compare(
            org.python.java.Collection x, org.python.Object y,
            org.python.types.Object.CMP_OP op) {
        java.util.Iterator<org.python.Object> xi = x.getCollection().iterator();
        java.util.Iterator<org.python.Object> yi = ((org.python.java.Collection) y).getCollection().iterator();

        // The comparison result of the first non-identical item is the result.
        while (xi.hasNext() && yi.hasNext()) {
            org.python.Object xn = xi.next();
            org.python.Object yn = yi.next();
            org.python.types.Bool equal = (org.python.types.Bool) org.python.types.Object.__cmp__(
                    xn, yn, org.python.types.Object.CMP_OP.EQ
            );
            if (equal.getInteger() == 0) {
                return org.python.types.Object.__cmp_bool__(xn, yn, op);
            }
        }

        // All are identical, break tie by size.
        return org.python.types.Object.__cmp_bool__(x.__len__(), y.__len__(), op);
    }

    public static org.python.Object lessThan(org.python.java.Collection x, org.python.Object y) {
        return compare(x, y, org.python.types.Object.CMP_OP.LT);
    }

    public static org.python.Object lessThanOrEqual(org.python.java.Collection x, org.python.Object y) {
        return compare(x, y, org.python.types.Object.CMP_OP.LE);
    }

    public static org.python.Object equal(org.python.java.Collection x, org.python.Object y) {
        return compare(x, y, org.python.types.Object.CMP_OP.EQ);
    }

    public static org.python.Object greaterThan(org.python.java.Collection x, org.python.Object y) {
        return compare(x, y, org.python.types.Object.CMP_OP.GT);
    }

    public static org.python.Object greaterThanOrEqual(org.python.java.Collection x, org.python.Object y) {
        return compare(x, y, org.python.types.Object.CMP_OP.GE);
    }

    /**
     * Handle index-based __getitem__.
     */
    public static org.python.Object getAtIndex(org.python.java.List x, org.python.Object index) {
        int idx = ((int) ((org.python.java.Integer) index).getInteger());
        java.util.List<org.python.Object> list = x.getList();
        if (idx < 0) {
            if (-idx > list.size()) {
                throw new org.python.exceptions.IndexError(String.format("%s index out of range", x.typeName()));
            } else {
                return list.get(list.size() + idx);
            }
        } else {
            if (idx >= list.size()) {
                throw new org.python.exceptions.IndexError(String.format("%s index out of range", x.typeName()));
            } else {
                return list.get(idx);
            }
        }
    }

    /**
     * Handle slice-based __getitem__
     */
    public static java.util.List<org.python.Object> getSlice(org.python.java.List x, org.python.Object index) {
        org.python.types.Slice.ValidatedValue val = ((org.python.types.Slice) index).validateValueTypes();
        java.util.List<org.python.Object> sliced = new java.util.ArrayList<org.python.Object>();
        if (val.start == null && val.stop == null && val.step == null) {
            sliced.addAll(x.getList());
        } else {
            org.python.types.Slice slice = new org.python.types.Slice(val.start, val.stop, val.step);
            org.python.types.Tuple indices = slice.indices((org.python.types.Int) (x.__len__()));
            long start = ((org.python.types.Int) (indices.value.get(0))).value;
            long stop = ((org.python.types.Int) (indices.value.get(1))).value;
            long step = ((org.python.types.Int) (indices.value.get(2))).value;
            /* If step is negative, we need to stop the loop when i <= stop.
             * If step is positive, we need to stop the loop when i >= stop.
             * validateValueTypes() has already ensured that step != 0.
             */
            int cmp = step > 0 ? -1 : 1;
            for (long i = start; (i > stop ? 1 : i < stop ? -1 : 0) == cmp; i += step) {
                sliced.add(x.getList().get((int) i));
            }
        }
        return sliced;
    }

    public static int getIndexValue(org.python.java.List x, org.python.Object index, int defaultValue, boolean wrap) {
        if (index == null) {
            return defaultValue;
        }
        if (index instanceof org.python.java.Integer) {
            int i = (int) ((org.python.java.Integer) index).getInteger();
            int size = x.getCollection().size();
            if (i < 0) {
                return wrap ? Math.max(0, Math.min(size, size + i)) : 0;
            }
            return Math.min(size, i);
        }

        if (org.Python.VERSION < 0x03050000) {
            throw new org.python.exceptions.TypeError(String.format(
                    "%s indices must be integers, not ",
                    x.typeName(), index.typeName()
            ));
        } else {
            throw new org.python.exceptions.TypeError(String.format(
                    "%s indices must be integers or slices, not ",
                    x.typeName(), index.typeName()
            ));
        }
    }

    public static org.python.Object getFirstIndexOf(
            org.python.java.List x, org.python.Object item, int start, int end) {
        java.util.ListIterator<org.python.Object> iterator = x.getList().listIterator();

        // Skip items.
        for (int skip = 0; skip < start && iterator.hasNext(); skip++) {
            iterator.next();
        }

        // Find matching item and return its index.
        int counter = end - start;
        while (counter > 0 && iterator.hasNext()) {
            org.python.Object current = iterator.next();
            org.python.Object eq = item.__eq__(current);
            if (eq instanceof org.python.types.Bool && ((org.python.types.Bool) eq).getInteger() != 0) {
                return new org.python.types.Int(iterator.previousIndex());
            }
            counter--;
        }

        return null;
    }
}
