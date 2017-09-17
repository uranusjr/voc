package org.python.types;

import org.python.internals.CollectionOperation;

public class Tuple extends org.python.types.Object implements org.python.java.List {
    public java.util.List<org.python.Object> value;

    public java.util.Collection<org.python.Object> getCollection() {
        return this.value;
    }

    public java.util.List<org.python.Object> getList() {
        return this.value;
    }

    public java.lang.Object toJava() {
        return this.value;
    }

    public int hashCode() {
        return this.value.hashCode();
    }

    public Tuple(java.util.List<org.python.Object> tuple) {
        super();
        this.value = tuple;
    }

    public Tuple() {
        this(new java.util.ArrayList<org.python.Object>());
    }

    @org.python.Method(
            __doc__ = "tuple() -> empty tuple" +
                    "tuple(iterable) -> tuple initialized from iterable's items\n" +
                    "\n" +
                    "If the argument is a tuple, the return value is the same object.\n"
    )
    public Tuple(org.python.Object[] args, java.util.Map<java.lang.String, org.python.Object> kwargs) {
        this();
        if (args[0] != null) {
            CollectionOperation.addAll(this, args[0]);
        }
    }

    // public org.python.Object __new__() {
    //     throw new org.python.exceptions.NotImplementedError("__new__() has not been implemented.");
    // }

    // public org.python.Object __init__() {
    //     throw new org.python.exceptions.NotImplementedError("__init__() has not been implemented.");
    // }

    @org.python.Method(
            __doc__ = "Return repr(self)."
    )
    public org.python.types.Str __repr__() {
        if (this.value.size() == 1) {
            return new org.python.types.Str(String.format("(%s,)", this.value.get(0).__repr__()));
        }
        return new org.python.types.Str(String.format(
            "(%s)", CollectionOperation.formatCommaSeperatedRepr(this)
        ));
    }

    @org.python.Method(
            __doc__ = "default object formatter"
    )
    public org.python.types.Str __format__(org.python.Object format_string) {
        throw new org.python.exceptions.NotImplementedError("__format__() has not been implemented.");
    }

    @org.python.Method(
            __doc__ = ""
    )
    public org.python.Object __pos__() {
        throw new org.python.exceptions.TypeError("bad operand type for unary +: 'tuple'");
    }

    @org.python.Method(
            __doc__ = ""
    )
    public org.python.Object __neg__() {
        throw new org.python.exceptions.TypeError("bad operand type for unary -: 'tuple'");
    }

    @org.python.Method(
            __doc__ = ""
    )
    public org.python.Object __invert__() {
        throw new org.python.exceptions.TypeError("bad operand type for unary ~: 'tuple'");
    }

    @org.python.Method(
            __doc__ = ""
    )
    public org.python.Object __bool__() {
        return new org.python.types.Bool(!this.value.isEmpty());
    }

    @org.python.Method(
            __doc__ = "Return self<value.",
            args = {"other"}
    )
    public org.python.Object __lt__(org.python.Object other) {
        if (other instanceof org.python.types.Tuple) {
            return CollectionOperation.lessThan(this, other);
        }
        return org.python.types.NotImplementedType.NOT_IMPLEMENTED;
    }

    @org.python.Method(
            __doc__ = "Return self<=value.",
            args = {"other"}
    )
    public org.python.Object __le__(org.python.Object other) {
        if (other instanceof org.python.types.Tuple) {
            return CollectionOperation.lessThanOrEqual(this, other);
        }
        return org.python.types.NotImplementedType.NOT_IMPLEMENTED;
    }

    @org.python.Method(
            __doc__ = "Return self==value.",
            args = {"other"}
    )
    public org.python.Object __eq__(org.python.Object other) {
        if (other instanceof org.python.types.Tuple) {
            return CollectionOperation.equal(this, other);
        }
        return org.python.types.NotImplementedType.NOT_IMPLEMENTED;
    }

    @org.python.Method(
            __doc__ = "Return self>value.",
            args = {"other"}
    )
    public org.python.Object __gt__(org.python.Object other) {
        if (other instanceof org.python.types.Tuple) {
            return CollectionOperation.greaterThan(this, other);
        }
        return org.python.types.NotImplementedType.NOT_IMPLEMENTED;
    }

    @org.python.Method(
            __doc__ = "Return self>=value.",
            args = {"other"}
    )
    public org.python.Object __ge__(org.python.Object other) {
        if (other instanceof org.python.types.Tuple) {
            return CollectionOperation.greaterThanOrEqual(this, other);
        }
        return org.python.types.NotImplementedType.NOT_IMPLEMENTED;
    }

    public boolean __setattr_null(java.lang.String name, org.python.Object value) {
        // Builtin types can't have attributes set on them.
        return false;
    }

    @org.python.Method(
            __doc__ = "__dir__() -> list\ndefault dir() implementation"
    )
    public org.python.types.List __dir__() {
        throw new org.python.exceptions.NotImplementedError("__dir__() has not been implemented.");
    }

    @org.python.Method(
            __doc__ = "Return len(self)."
    )
    public org.python.types.Int __len__() {
        return new org.python.types.Int(this.value.size());
    }

    @org.python.Method(
            __doc__ = "Return self[key].",
            args = {"index"}
    )
    public org.python.Object __getitem__(org.python.Object index) {
        if (index instanceof org.python.types.Slice) {
            return new org.python.types.Tuple(CollectionOperation.getSlice(this, index));
        } else if (index instanceof org.python.java.Integer) {
            return CollectionOperation.getAtIndex(this, index);
        }

        if (org.Python.VERSION < 0x03050000) {
            throw new org.python.exceptions.TypeError(String.format(
                    "tuple indices must be integers, not %s",
                    index.typeName()
            ));
        } else {
            throw new org.python.exceptions.TypeError(String.format(
                    "tuple indices must be integers or slices, not %s",
                    index.typeName()
            ));
        }
    }

    @org.python.Method(
            __doc__ = "",
            args = {"index", "value"}
    )
    public void __setitem__(org.python.Object index, org.python.Object value) {
        throw new org.python.exceptions.TypeError(
                "'tuple' object does not support item assignment"
        );
    }

    @org.python.Method(
            __doc__ = "",
            args = {"item"}
    )
    public void __delitem__(org.python.Object item) {
        throw new org.python.exceptions.TypeError("'tuple' object doesn't support item deletion");
    }

    @org.python.Method(
            __doc__ = "Implement iter(self)."
    )
    public org.python.Object __iter__() {
        return new Iterator(this);
    }

    @org.python.Method(
            __doc__ = "Return key in self.",
            args = {"item"}
    )
    public org.python.Object __contains__(org.python.Object item) {
        return new org.python.types.Bool(this.value.contains(item));
    }

    @org.python.Method(
            __doc__ = "Return self+value.",
            args = {"other"}
    )
    public org.python.Object __add__(org.python.Object other) {
        if (other instanceof org.python.types.Tuple) {
            org.python.types.Tuple result = new org.python.types.Tuple(this.value);
            result.getList().addAll(((org.python.types.Tuple) other).value);
            return result;
        } else {
            throw new org.python.exceptions.TypeError(String.format(
                    "can only concatenate tuple (not \"%s\") to tuple", other.typeName()
            ));
        }
    }

    @org.python.Method(
            __doc__ = "Return self*value.n",
            args = {"other"}
    )
    public org.python.Object __mul__(org.python.Object other) {
        if (other instanceof org.python.types.Int) {
            long count = ((org.python.types.Int) other).value;
            org.python.types.Tuple result = new org.python.types.Tuple();
            for (long i = 0; i < count; i++) {
                result.value.addAll(this.value);
            }
            return result;
        } else if (other instanceof org.python.types.Bool) {
            boolean count = ((org.python.types.Bool) other).value;
            org.python.types.Tuple result = new org.python.types.Tuple();
            if (count) {
                result.value.addAll(this.value);
            }
            return result;
        }
        throw new org.python.exceptions.TypeError("can't multiply sequence by non-int of type '" + other.typeName() + "'");
    }

    @org.python.Method(
            __doc__ = "",
            args = {"other"}
    )
    public org.python.Object __imul__(org.python.Object other) {
        if (other instanceof org.python.types.Int) {
            long count = ((org.python.types.Int) other).value;
            org.python.types.Tuple result = new org.python.types.Tuple();
            for (long i = 0; i < count; i++) {
                result.value.addAll(this.value);
            }
            return result;
        } else if (other instanceof org.python.types.Bool) {
            boolean bool = ((org.python.types.Bool) other).value;
            if (bool) {
                return this;
            } else {
                return new org.python.types.Tuple();
            }
        } else {
            throw new org.python.exceptions.TypeError("can't multiply sequence by non-int of type '" + other.typeName() + "'");
        }
    }


    @org.python.Method(
            __doc__ = "Return self*value.",
            args = {"other"}
    )
    public org.python.Object __rmul__(org.python.Object other) {
        throw new org.python.exceptions.NotImplementedError("__rmul__() has not been implemented.");
    }

    @org.python.Method(
            __doc__ = "T.count(value) -> integer -- return number of occurrences of value"
    )
    public org.python.Object count() {
        return new org.python.types.Int(this.value.size());
    }

    @org.python.Method(
            __doc__ = "index of the first occurrence of x in s (at or after index i and before index j)",
            default_args = {"item", "start", "end"}
    )
    public org.python.Object index(org.python.Object item, org.python.Object start, org.python.Object end) {
        if (item == null) {     // Different error message from default.
            throw new org.python.exceptions.TypeError("index() takes at least 1 argument (0 given)");
        }
        org.python.Object index = CollectionOperation.getFirstIndexOf(
                this, item,
                CollectionOperation.getIndexValue(this, start, 0, false),
                CollectionOperation.getIndexValue(this, end, this.value.size(), true)
        );
        if (index == null) {
            throw new org.python.exceptions.ValueError("tuple.index(x): x not in tuple");
        }
        return index;
    }

    @org.python.Method(
            __doc__ = ""
    )
    public org.python.Object __round__(org.python.Object ndigits) {

        throw new org.python.exceptions.TypeError("type tuple doesn't define __round__ method");
    }

    public class Iterator extends org.python.types.Iterator {
        public static final java.lang.String PYTHON_TYPE_NAME = "tuple_iterator";

        public Iterator(org.python.types.Tuple tuple) {
            this.iterator = tuple.value.iterator();
        }
    }
}
