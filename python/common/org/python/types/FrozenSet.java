package org.python.types;

import org.python.internals.SetOperation;

public class FrozenSet extends org.python.types.Object implements org.python.java.Set {
    public java.util.Set<org.python.Object> value;

    public java.util.Collection<org.python.Object> getCollection() {
        return this.value;
    }

    public java.util.Set<org.python.Object> getSet() {
        return this.value;
    }

    /**
     * A utility method to update the internal value of this object.
     *
     * Used by __i*__ operations to do an in-place operation.
     * obj must be of type org.python.types.FrozenSet
     */
    void setValue(org.python.Object obj) {
        this.value = ((org.python.types.FrozenSet) obj).value;
    }

    public java.lang.Object toJava() {
        return this.value;
    }

    public int hashCode() {
        return this.value.hashCode();
    }

    public FrozenSet() {
        super();
        this.value = java.util.Collections.emptySet();
    }

    public FrozenSet(java.util.Set<org.python.Object> frozenSet) {
        super();
        this.value = java.util.Collections.unmodifiableSet(frozenSet);
    }

    @org.python.Method(
            __doc__ = "frozenset() -> empty frozenset object" +
                    "frozenset(iterable) -> frozenset object\n" +
                    "\n" +
                    "Build an immutable unordered collection of unique elements.\n",
            default_args = {"iterable"}
    )
    public FrozenSet(org.python.Object[] args, java.util.Map<java.lang.String, org.python.Object> kwargs) {
        if (args[0] == null) {
            this.value = java.util.Collections.emptySet();
        } else {
            if (args[0] instanceof org.python.types.Set) {
                this.value = java.util.Collections.unmodifiableSet(
                        ((org.python.types.Set) args[0]).value
                );
            } else if (args[0] instanceof org.python.types.List) {
                this.value = java.util.Collections.unmodifiableSet(
                        new java.util.HashSet<org.python.Object>(
                        ((org.python.types.List) args[0]).value)
                );
            } else if (args[0] instanceof org.python.types.Tuple) {
                this.value = java.util.Collections.unmodifiableSet(
                        new java.util.HashSet<org.python.Object>(
                        ((org.python.types.Tuple) args[0]).value)
                );
            } else {
                org.python.Object iterator = org.Python.iter(args[0]);
                java.util.Set<org.python.Object> generated = new java.util.HashSet<org.python.Object>();
                try {
                    while (true) {
                        org.python.Object next = iterator.__next__();
                        generated.add(next);
                    }
                } catch (org.python.exceptions.StopIteration si) {
                }
                this.value = java.util.Collections.unmodifiableSet(generated);
            }
        }
    }

    @org.python.Method(
            __doc__ = "",
            args = {"index"}
    )
    public org.python.Object __getitem__(org.python.Object index) {
        if (index instanceof org.python.java.Integer) {
            throw new org.python.exceptions.TypeError("'frozenset' object does not support indexing");
        } else {
            throw new org.python.exceptions.TypeError("'frozenset' object is not subscriptable");
        }
    }

    @org.python.Method(
            __doc__ = ""
    )
    public org.python.Object __invert__() {
        throw new org.python.exceptions.TypeError("bad operand type for unary ~: 'frozenset'");
    }

    @org.python.Method(
            __doc__ = "Implement iter(self)."
    )
    public org.python.Object __iter__() {
        return new org.python.types.Set_Iterator(this);
    }

    @org.python.Method(
            __doc__ = ""
    )
    public org.python.Object __pos__() {
        throw new org.python.exceptions.TypeError("bad operand type for unary +: 'frozenset'");
    }

    @org.python.Method(
            __doc__ = ""
    )
    public org.python.Object __neg__() {
        throw new org.python.exceptions.TypeError("bad operand type for unary -: 'frozenset'");
    }

    @org.python.Method(
            __doc__ = ""
    )
    public org.python.types.Bool __bool__() {
        return new org.python.types.Bool(this.value.size() > 0);
    }

    @org.python.Method(
            __doc__ = "Return repr(self)."
    )
    public org.python.types.Str __repr__() {
        // Representation of an empty set is different
        if (this.value.size() == 0) {
            return new org.python.types.Str("frozenset()");
        }

        java.lang.StringBuilder buffer = new java.lang.StringBuilder("frozenset({");
        boolean first = true;
        for (org.python.Object obj : this.value) {
            if (first) {
                first = false;
            } else {
                buffer.append(", ");
            }
            buffer.append(obj.__repr__());
        }
        buffer.append("})");
        return new org.python.types.Str(buffer.toString());
    }

    @org.python.Method(
            __doc__ = "Return len(self)."
    )
    public org.python.types.Int __len__() {
        return new org.python.types.Int(this.value.size());
    }

    @org.python.Method(
            __doc__ = "x.__contains__(y) <==> y in x.",
            args = {"item"}
    )
    public org.python.Object __contains__(org.python.Object other) {
        return new org.python.types.Bool(this.value.contains(other));
    }


    @org.python.Method(
            __doc__ = "",
            args = {"item"}
    )
    public org.python.Object __not_contains__(org.python.Object other) {
        return new org.python.types.Bool(!this.value.contains(other));
    }

    @org.python.Method(
            __doc__ = "Return a shallow copy of a FrozenSet."
    )
    public org.python.Object copy() {
        return this;
    }

    @org.python.Method(
            __doc__ = "Return self<value.",
            args = {"other"}
    )
    public org.python.Object __lt__(org.python.Object other) {
        return SetOperation.lessThan(this, other);
    }

    @org.python.Method(
            __doc__ = "Return self<=value.",
            args = {"other"}
    )
    public org.python.Object __le__(org.python.Object other) {
        return SetOperation.lessThanOrEqual(this, other);
    }

    @org.python.Method(
            __doc__ = "Return self==value.",
            args = {"other"}
    )
    public org.python.Object __eq__(org.python.Object other) {
        return SetOperation.equal(this, other);
    }

    @org.python.Method(
            __doc__ = "Return self>value.",
            args = {"other"}
    )
    public org.python.Object __gt__(org.python.Object other) {
        return SetOperation.greaterThan(this, other);
    }

    @org.python.Method(
            __doc__ = "Return self>=value.",
            args = {"other"}
    )
    public org.python.Object __ge__(org.python.Object other) {
        return SetOperation.greaterThanOrEqual(this, other);
    }

    @org.python.Method(
            __doc__ = "",
            args = {"other"}
    )
    public org.python.Object __mul__(org.python.Object other) {
        if (other instanceof org.python.types.List ||
                other instanceof org.python.types.Tuple ||
                other instanceof org.python.types.Str ||
                other instanceof org.python.types.Bytes ||
                other instanceof org.python.types.ByteArray
            ) {
            throw new org.python.exceptions.TypeError("can't multiply sequence by non-int of type '" + this.typeName() + "'");
        }
        throw new org.python.exceptions.TypeError("unsupported operand type(s) for *: '" + this.typeName() + "' and '" + other.typeName() + "'");
    }

    @org.python.Method(
            __doc__ = "Return self-value."
    )
    public org.python.Object __sub__(org.python.Object other) {
        return new org.python.types.FrozenSet(SetOperation.substract(this, other, false));
    }

    @org.python.Method(
            __doc__ = "Return self&value."
    )
    public org.python.Object __and__(org.python.Object other) {
        return new org.python.types.FrozenSet(SetOperation.and(this, other, false));
    }

    @org.python.Method(
            __doc__ = "Return self^value."
    )
    public org.python.Object __xor__(org.python.Object other) {
        return new org.python.types.FrozenSet(SetOperation.exclusiveOr(this, other, false));
    }

    @org.python.Method(
            __doc__ = "Return self|value."
    )
    public org.python.Object __or__(org.python.Object other) {
        return new org.python.types.FrozenSet(SetOperation.or(this, other, false));
    }

    @org.python.Method(
            __doc__ = "Return True if the set has no elements in common with other. Sets are\n" +
                      "disjoint if and only if their intersection is the empty set.",
            args = {"other"}
    )
    public org.python.Object isdisjoint(org.python.Object other) {
        return new org.python.types.Bool(SetOperation.intersect(this, other, false).isEmpty());
    }

    @org.python.Method(
            __doc__ = "Whether every element in the set is in other.",
            args = {"other"}
    )
    public org.python.Object issubset(org.python.Object other) {
        return new org.python.types.Bool(SetOperation.different(this, other, false).isEmpty());
    }

    @org.python.Method(
            __doc__ = "Whether every element in other is in the set",
            args = {"other"}
    )
    public org.python.Object issuperset(org.python.Object other) {
        return new org.python.types.Bool(SetOperation.unify(this, other, false).equals(this.value));
    }

    @org.python.Method(
            __doc__ = "Return the intersection of two sets as a new set.\n\n(i.e. all elements that are in both sets.)",
            varargs = "others"
    )
    public org.python.Object intersection(org.python.types.Tuple others) {
        java.util.Set<org.python.Object> set = new java.util.HashSet<org.python.Object>(this.getSet());
        java.util.Iterator<org.python.Object> iterator = ((org.python.types.Iterator) others.__iter__()).getIterator();
        while (iterator.hasNext()) {
            SetOperation.intersect(set, iterator.next());
        }
        return new org.python.types.FrozenSet(set);
    }

    @org.python.Method(
            __doc__ = "Return a new set with elements in either the set or other but not both.",
            default_args = {"other"}
    )
    public org.python.Object symmetric_difference(org.python.Object other) {
        if (other == null) {    // CPython uses a message different from the default.
            throw new org.python.exceptions.TypeError("symmetric_difference() takes exactly one argument (0 given)");
        }
        return new org.python.types.FrozenSet(SetOperation.symmetricDifferent(this, other, false));
    }

    @org.python.Method(
            __doc__ = "Return the difference of two or more sets as a new set.\n\n(i.e. all elements that are in this set but not the others.)",
            varargs = "others"
    )
    public org.python.Object difference(org.python.types.Tuple others) {
        java.util.Set<org.python.Object> set = new java.util.HashSet<org.python.Object>(this.getSet());
        java.util.Iterator<org.python.Object> iterator = ((org.python.types.Iterator) others.__iter__()).getIterator();
        while (iterator.hasNext()) {
            SetOperation.different(set, iterator.next());
        }
        return new org.python.types.FrozenSet(set);
    }

    @org.python.Method(
            __doc__ = "Return the union of sets as a new set.\n\n(i.e. all elements that are in either set.)",
            varargs = "others"
    )
    public org.python.Object union(org.python.types.Tuple others) {
        java.util.Set<org.python.Object> set = new java.util.HashSet<org.python.Object>(this.getSet());
        java.util.Iterator<org.python.Object> iterator = ((org.python.types.Iterator) others.__iter__()).getIterator();
        while (iterator.hasNext()) {
            SetOperation.unify(set, iterator.next());
        }
        return new org.python.types.FrozenSet(set);
    }
}
