package org.python.internals;

public class SetOperation {
    /**
     * Helper to get a set instance to manupilate on.
     * If inplace is true, this uses x's underlying data directly, otherwise a copy is performed.
     * This is a simple optimization to save copying if possible.
     */
    private static java.util.Set<org.python.Object> getSet(org.python.java.Set x, boolean inplace) {
        java.util.Set<org.python.Object> set = x.getSet();
        return inplace ? set : new java.util.HashSet<org.python.Object>(set);
    }

    public static org.python.Object lessThan(org.python.java.Set x, org.python.Object y) {
        if (y instanceof org.python.java.Set) {
            java.util.Set<org.python.Object> xs = x.getSet();
            java.util.Set<org.python.Object> ys = ((org.python.java.Set) y).getSet();
            return new org.python.types.Bool(ys.containsAll(xs) && !xs.equals(ys));
        }
        return org.python.types.NotImplementedType.NOT_IMPLEMENTED;
    }

    public static org.python.Object lessThanOrEqual(org.python.java.Set x, org.python.Object y) {
        if (y instanceof org.python.java.Set) {
            java.util.Set<org.python.Object> xs = x.getSet();
            java.util.Set<org.python.Object> ys = ((org.python.java.Set) y).getSet();
            return new org.python.types.Bool(ys.containsAll(xs));
        }
        return org.python.types.NotImplementedType.NOT_IMPLEMENTED;
    }

    public static org.python.Object equal(org.python.java.Set x, org.python.Object y) {
        if (y instanceof org.python.java.Set) {
            java.util.Set<org.python.Object> xs = x.getSet();
            java.util.Set<org.python.Object> ys = ((org.python.java.Set) y).getSet();
            return new org.python.types.Bool(xs.equals(ys));
        }
        return org.python.types.NotImplementedType.NOT_IMPLEMENTED;
    }

    public static org.python.Object greaterThan(org.python.java.Set x, org.python.Object y) {
        if (y instanceof org.python.java.Set) {
            java.util.Set<org.python.Object> xs = x.getSet();
            java.util.Set<org.python.Object> ys = ((org.python.java.Set) y).getSet();
            return new org.python.types.Bool(xs.containsAll(ys) && !xs.equals(ys));
        }
        return org.python.types.NotImplementedType.NOT_IMPLEMENTED;
    }

    public static org.python.Object greaterThanOrEqual(org.python.java.Set x, org.python.Object y) {
        if (y instanceof org.python.java.Set) {
            java.util.Set<org.python.Object> xs = x.getSet();
            java.util.Set<org.python.Object> ys = ((org.python.java.Set) y).getSet();
            return new org.python.types.Bool(xs.containsAll(ys));
        }
        return org.python.types.NotImplementedType.NOT_IMPLEMENTED;
    }

    public static java.util.Set<org.python.Object> substract(org.python.java.Set x, org.python.Object y, boolean inplace) {
        if (y instanceof org.python.java.Set) {
            java.util.Set<org.python.Object> set = getSet(x, inplace);
            set.removeAll(((org.python.java.Set) y).getSet());
            return set;
        }
        throw new org.python.exceptions.TypeError(String.format(
                "unsupported operand type(s) for %s: '%s' and '%s'",
                inplace ? "-=" : "-", x.typeName(), y.typeName()
        ));
    }

    public static java.util.Set<org.python.Object> and(org.python.java.Set x, org.python.Object y, boolean inplace) {
        if (y instanceof org.python.java.Set) {
            java.util.Set<org.python.Object> set = getSet(x, inplace);
            set.retainAll(((org.python.java.Set) y).getSet());
            return set;
        }
        throw new org.python.exceptions.TypeError(String.format(
                "unsupported operand type(s) for %s: '%s' and '%s'",
                inplace ? "&=" : "&", x.typeName(), y.typeName()
        ));
    }

    public static java.util.Set<org.python.Object> exclusiveOr(org.python.java.Set x, org.python.Object y, boolean inplace) {
        if (y instanceof org.python.java.Set) {
            java.util.Set<org.python.Object> xSet = getSet(x, inplace);
            java.util.Iterator<org.python.Object> yIterator = ((org.python.java.Set) y).getSet().iterator();
            while (yIterator.hasNext()) {
                org.python.Object object = yIterator.next();
                if (xSet.contains(object)) {
                    xSet.remove(object);
                } else {
                    xSet.add(object);
                }
            }
            return xSet;
        }
        throw new org.python.exceptions.TypeError(String.format(
                "unsupported operand type(s) for %s: '%s' and '%s'",
                inplace ? "^=" : "^", x.typeName(), y.typeName()
        ));
    }

    public static java.util.Set<org.python.Object> or(org.python.java.Set x, org.python.Object y, boolean inplace) {
        if (y instanceof org.python.java.Set) {
            java.util.Set<org.python.Object> set = getSet(x, inplace);
            set.addAll(((org.python.java.Set) y).getSet());
            return set;
        }
        throw new org.python.exceptions.TypeError(String.format(
                "unsupported operand type(s) for %s: '%s' and '%s'",
                inplace ? "|=" : "|", x.typeName(), y.typeName()
        ));
    }

    public static java.util.Set<org.python.Object> different(org.python.java.Set x, org.python.Object y, boolean inplace) {
        java.util.Set<org.python.Object> set = getSet(x, inplace);
        different(set, y);
        return set;
    }

    public static void different(java.util.Set<org.python.Object> set, org.python.Object y) {
        if (y instanceof org.python.java.Collection) {
            set.removeAll(((org.python.java.Collection) y).getCollection());
            return;
        }

        org.python.Object iter = CollectionOperation.getIter(y);
        while (true) {
            try {
                set.remove(iter.__next__());
            } catch (org.python.exceptions.StopIteration si) {
                break;
            }
        }
    }

    public static java.util.Set<org.python.Object> intersect(org.python.java.Set x, org.python.Object y, boolean inplace) {
        java.util.Set<org.python.Object> set = getSet(x, inplace);
        intersect(set, y);
        return set;
    }

    public static void intersect(java.util.Set<org.python.Object> set, org.python.Object y) {
        java.util.Collection<org.python.Object> collection;
        if (y instanceof org.python.java.Collection) {
            collection = ((org.python.java.Collection) y).getCollection();
        } else {
            org.python.Object iter = CollectionOperation.getIter(y);
            collection = new java.util.HashSet<org.python.Object>();
            while (true) {
                try {
                    collection.add(iter.__next__());
                } catch (org.python.exceptions.StopIteration si) {
                    break;
                }
            }
        }
        set.retainAll(collection);
    }

    public static java.util.Set<org.python.Object> symmetricDifferent(org.python.java.Set x, org.python.Object y, boolean inplace) {
        java.util.Set<org.python.Object> set = getSet(x, inplace);
        org.python.Object iter = null;
        if (y instanceof org.python.java.Collection) {
            iter = ((org.python.java.Collection) y).__iter__();
        } else {
            iter = CollectionOperation.getIter(y);
        }
        while (true) {
            org.python.Object object;
            try {
                object = iter.__next__();
            } catch (org.python.exceptions.StopIteration si) {
                break;
            }
            if (set.contains(object)) {
                set.remove(object);
            } else {
                set.add(object);
            }
        }
        return set;
    }

    public static java.util.Set<org.python.Object> unify(org.python.java.Set x, org.python.Object y, boolean inplace) {
        java.util.Set<org.python.Object> set = getSet(x, inplace);
        unify(set, y);
        return set;
    }

    public static void unify(java.util.Set<org.python.Object> set, org.python.Object y) {
        if (y instanceof org.python.java.Collection) {
            set.addAll(((org.python.java.Collection) y).getCollection());
            return;
        }
        org.python.Object iter = CollectionOperation.getIter(y);
        while (true) {
            try {
                set.add(iter.__next__());
            } catch (org.python.exceptions.StopIteration si) {
                break;
            }
        }
    }
}
