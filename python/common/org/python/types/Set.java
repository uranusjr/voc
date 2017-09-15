package org.python.types;

import org.python.internals.CollectionOperation;
import org.python.internals.SetOperation;

public class Set extends org.python.types.Object implements org.python.java.Set {
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
     * obj must be of type org.python.types.Set
     */
    void setValue(org.python.Object obj) {
        this.value = ((org.python.types.Set) obj).value;
    }

    public java.lang.Object toJava() {
        return this.value;
    }

    public int hashCode() {
        return this.value.hashCode();
    }

    @Override
    public org.python.Object __hash__() {
        throw new org.python.exceptions.AttributeError(this, "__hash__");
    }

    public Set(java.util.Set<org.python.Object> set) {
        super();
        this.value = set;
    }

    public Set() {
        this(new java.util.HashSet<org.python.Object>());
    }

    @org.python.Method(
            __doc__ = "set() -> new empty set object" +
                    "set(iterable) -> new set object\n" +
                    "\n" +
                    "Build an unordered collection of unique elements.\n",
            default_args = {"iterable"}
    )
    public Set(org.python.Object[] args, java.util.Map<java.lang.String, org.python.Object> kwargs) {
        this();
        if (args[0] != null){
            CollectionOperation.addAll(this, args[0]);
        }
    }

    // public org.python.Object __new__() {
    //     throw new org.python.exceptions.NotImplementedError("__new__() has not been implemented");
    // }

    // public org.python.Object __init__() {
    //     throw new org.python.exceptions.NotImplementedError("__init__() has not been implemented");
    // }

    @org.python.Method(
            __doc__ = "Return repr(self)."
    )
    public org.python.types.Str __repr__() {
        // Representation of an empty set is different
        if (this.value.size() == 0) {
            return new org.python.types.Str("set()");
        }

        java.lang.StringBuilder buffer = new java.lang.StringBuilder("{");
        boolean first = true;
        for (org.python.Object obj : this.value) {
            if (first) {
                first = false;
            } else {
                buffer.append(", ");
            }
            buffer.append(obj.__repr__());
        }
        buffer.append("}");
        return new org.python.types.Str(buffer.toString());
    }

    @org.python.Method(
            __doc__ = "default object formatter"
    )
    public org.python.types.Str __format__(org.python.Object format_string) {
        throw new org.python.exceptions.NotImplementedError("__format__() has not been implemented");
    }

    @org.python.Method(
            __doc__ = "",
            args = {"index"}
    )
    public org.python.Object __getitem__(org.python.Object index) {
        if (index instanceof org.python.java.Integer) {
            throw new org.python.exceptions.TypeError("'set' object does not support indexing");
        } else {
            throw new org.python.exceptions.TypeError("'set' object is not subscriptable");
        }
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

    public boolean __setattr_null(java.lang.String name, org.python.Object value) {
        // Builtin types can't have attributes set on them.
        return false;
    }

    @org.python.Method(
            __doc__ = ""
    )
    public org.python.types.Bool __bool__() {
        return new org.python.types.Bool(this.value.size() > 0);
    }

    @org.python.Method(
            __doc__ = ""
    )
    public org.python.Object __invert__() {
        throw new org.python.exceptions.TypeError("bad operand type for unary ~: 'set'");
    }

    @org.python.Method(
            __doc__ = ""
    )
    public org.python.Object __pos__() {
        throw new org.python.exceptions.TypeError("bad operand type for unary +: 'set'");
    }

    @org.python.Method(
            __doc__ = ""
    )
    public org.python.Object __neg__() {
        throw new org.python.exceptions.TypeError("bad operand type for unary -: 'set'");
    }

    @org.python.Method(
            __doc__ = "__dir__() -> list\ndefault dir() implementation"
    )
    public org.python.types.List __dir__() {
        throw new org.python.exceptions.NotImplementedError("__dir__() has not been implemented");
    }

    @org.python.Method(
            __doc__ = "Return len(self)."
    )
    public org.python.types.Int __len__() {
        return new org.python.types.Int(this.value.size());
    }

    @org.python.Method(
            __doc__ = "Implement iter(self)."
    )
    public org.python.Object __iter__() {
        return new org.python.types.Set_Iterator(this);
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
            __doc__ = ""
    )
    public org.python.Object __mul__(org.python.Object other) {
        if ((other instanceof org.python.types.List) ||
                (other instanceof org.python.types.Tuple) ||
                (other instanceof org.python.types.Str) ||
                (other instanceof org.python.types.ByteArray) ||
                (other instanceof org.python.types.Bytes)) {
            throw new org.python.exceptions.TypeError("can't multiply sequence by non-int of type '" + this.typeName() + "'");
        }
        return super.__mul__(other);
    }

    @org.python.Method(
            __doc__ = "Return self-value."
    )
    public org.python.Object __sub__(org.python.Object other) {
        return new org.python.types.Set(SetOperation.substract(this, other, false));
    }

    @org.python.Method(
            __doc__ = "Return self&value."
    )
    public org.python.Object __and__(org.python.Object other) {
        return new org.python.types.Set(SetOperation.and(this, other, false));
    }

    @org.python.Method(
            __doc__ = "Return self^value."
    )
    public org.python.Object __xor__(org.python.Object other) {
        return new org.python.types.Set(SetOperation.exclusiveOr(this, other, false));
    }

    @org.python.Method(
            __doc__ = "Return self|value."
    )
    public org.python.Object __or__(org.python.Object other) {
        return new org.python.types.Set(SetOperation.or(this, other, false));
    }

    // @org.python.Method(
    //     __doc__ = ""
    // )
    // public org.python.Object __rsub__(org.python.Object other) {
    //     throw new org.python.exceptions.NotImplementedError("__rsub__() has not been implemented");
    // }

    // @org.python.Method(
    //     __doc__ = ""
    // )
    // public org.python.Object __rand__(org.python.Object other) {
    //     throw new org.python.exceptions.NotImplementedError("__rand__() has not been implemented");
    // }

    // @org.python.Method(
    //     __doc__ = ""
    // )
    // public org.python.Object __rxor__(org.python.Object other) {
    //     throw new org.python.exceptions.NotImplementedError("__rxor__() has not been implemented");
    // }

    // @org.python.Method(
    //     __doc__ = ""
    // )
    // public org.python.Object __ror__(org.python.Object other) {
    //     throw new org.python.exceptions.NotImplementedError("__ror__() has not been implemented");
    // }

    // @org.python.Method(
    //     __doc__ = ""
    // )
    // public void __ixor__(org.python.Object other) {
    //     throw new org.python.exceptions.NotImplementedError("__ixor__() has not been implemented");
    // }

    @org.python.Method(
            __doc__ = "Add an element to a set.\n\nThis has no effect if the element is already present.",
            args = {"other"}
    )
    public org.python.Object add(org.python.Object other) {
        this.value.add(other);
        return org.python.types.NoneType.NONE;
    }

    @org.python.Method(
            __doc__ = "Remove all elements from this set."
    )
    public org.python.Object clear() {
        this.value.clear();
        return org.python.types.NoneType.NONE;
    }

    @org.python.Method(
            __doc__ = "Return a shallow copy of a set."
    )
    public org.python.Object copy() {
        return new Set(new java.util.HashSet<org.python.Object>(this.value));
    }

    @org.python.Method(
            __doc__ = "Remove an element from a set if it is a member.\n\nIf the element is not a member, do nothing.",
            args = {"item"}
    )
    public org.python.Object discard(org.python.Object item) {
        this.value.remove(item);
        return org.python.types.NoneType.NONE;
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
        return new org.python.types.Set(set);
    }

    @org.python.Method(
            __doc__ = "Update a set with the intersection of itself and another.",
            args = {"other"}
    )
    public org.python.Object intersection_update(org.python.Object other) {
        if (other != null) {
            SetOperation.intersect(this, other, true);
        }
        return org.python.types.NoneType.NONE;
    }

    @org.python.Method(
            __doc__ = "Return a new set with elements in either the set or other but not both.",
            default_args = {"other"}
    )
    public org.python.Object symmetric_difference(org.python.Object other) {
        if (other == null) {    // CPython uses a message different from the default.
            throw new org.python.exceptions.TypeError("symmetric_difference() takes exactly one argument (0 given)");
        }
        return new org.python.types.Set(SetOperation.symmetricDifferent(this, other, false));
    }

    @org.python.Method(
            __doc__ = "Update the set, keeping only elements found in either set, but not in both.",
            default_args = {"other"}
    )
    public org.python.Object symmetric_difference_update(org.python.Object other) {
        if (other == null) {    // CPython uses a message different from the default.
            throw new org.python.exceptions.TypeError("symmetric_difference_update() takes exactly one argument (0 given)");
        }
        SetOperation.symmetricDifferent(this, other, true);
        return org.python.types.NoneType.NONE;
    }

    @org.python.Method(
            __doc__ = "Return True if the set has no elements in common with other. Sets are disjoint if and only if their intersection is the empty set.",
            default_args = {"other"}
    )
    public org.python.Object isdisjoint(org.python.Object other) {
        return new org.python.types.Bool(SetOperation.intersect(this, other, false).isEmpty());
    }

    @org.python.Method(
            __doc__ = "Test whether every element in the set is in other.",
            default_args = {"other"}
    )
    public org.python.Object issubset(org.python.Object other) {
        return new org.python.types.Bool(SetOperation.different(this, other, false).isEmpty());
    }

    @org.python.Method(
            __doc__ = "Test whether every element in other is in the set.",
            default_args = {"other"}
    )
    public org.python.Object issuperset(org.python.Object other) {
        return new org.python.types.Bool(SetOperation.unify(this, other, false).equals(this.value));
    }

    @org.python.Method(
            __doc__ = "Remove and return an arbitrary set element.\nRaises KeyError if the set is empty.",
            args = {}
    )
    public org.python.Object pop() {
        if (this.value.size() == 0) {
            throw new org.python.exceptions.KeyError(new org.python.types.Str("pop from an empty set"));
        }

        java.util.Iterator<org.python.Object> iterator = this.value.iterator();
        org.python.Object popped = iterator.next();
        iterator.remove();

        return popped;
    }

    @org.python.Method(
            __doc__ = "Remove an element from a set; it must be a member.\n\nIf the element is not a member, raise a KeyError.",
            args = {"item"}
    )
    public org.python.Object remove(org.python.Object item) {
        if (!this.value.remove(item)) {
            throw new org.python.exceptions.KeyError(item);
        }
        return org.python.types.NoneType.NONE;
    }

    @org.python.Method(
            __doc__ = "Return the difference of two or more sets as a new set.\n\n(i.e. all elements that are in this set but not the others.)",
            varargs = "others"
    )
    public org.python.Object difference(org.python.types.Tuple others) {
        java.util.Set<org.python.Object> set = new java.util.HashSet<org.python.Object>(this.value);
        java.util.Iterator<org.python.Object> iterator = ((org.python.types.Iterator) others.__iter__()).getIterator();
        while (iterator.hasNext()) {
            SetOperation.different(set, iterator.next());
        }
        return new org.python.types.Set(set);
    }

    @org.python.Method(
            __doc__ = "Remove all elements of another set from this set.",
            varargs = "others"
    )
    public org.python.Object difference_update(org.python.types.Tuple others) {
        java.util.Set<org.python.Object> set = this.value;
        java.util.Iterator<org.python.Object> iterator = ((org.python.types.Iterator) others.__iter__()).getIterator();
        while (iterator.hasNext()) {
            SetOperation.different(set, iterator.next());
        }
        return org.python.types.NoneType.NONE;
    }

    @org.python.Method(
            __doc__ = "Update a set with the union of itself and others.",
            varargs = "others"
    )
    public org.python.Object update(org.python.types.Tuple others) {
        java.util.Set<org.python.Object> set = this.value;
        java.util.Iterator<org.python.Object> iterator = ((org.python.types.Iterator) others.__iter__()).getIterator();
        while (iterator.hasNext()) {
            SetOperation.unify(set, iterator.next());
        }
        return org.python.types.NoneType.NONE;
    }

    @org.python.Method(
            __doc__ = "Return the union of sets as a new set.\n\n(i.e. all elements that are in either set.)",
            varargs = "others"
    )
    public org.python.Object union(org.python.types.Tuple others) {
        java.util.Set<org.python.Object> set = new java.util.HashSet<org.python.Object>(this.value);
        java.util.Iterator<org.python.Object> iterator = ((org.python.types.Iterator) others.__iter__()).getIterator();
        while (iterator.hasNext()) {
            SetOperation.unify(set, iterator.next());
        }
        return new org.python.types.Set(set);
    }
}
