package org.python.types;

import org.python.internals.IntegerOperation;

public class Int extends org.python.types.Object implements org.python.java.Integer {
    public long value;

    public long getInteger() {
        return this.value;
    }

    public double getFloat() {
        return (double) this.value;
    }

    public java.lang.Object toJava() {
        return this.value;
    }

    public org.python.Object byValue() {
        return new org.python.types.Int(this.value);
    }

    public int hashCode() {
        return new java.lang.Long(this.value).hashCode();
    }

    public Int(byte value) {
        this.value = (long) value;
    }

    public Int(short value) {
        this.value = (long) value;
    }

    public Int(int value) {
        this.value = (long) value;
    }

    public Int(long value) {
        this.value = value;
    }

    @org.python.Method(
            name = "int",
            __doc__ = "int(x=0) -> integer" +
                    "int(x, base=10) -> integer\n" +
                    "\n" +
                    "Convert a number or string to an integer, or return 0 if no arguments\n" +
                    "are given.  If x is a number, return x.__int__().  For floating point\n" +
                    "numbers, this truncates towards zero.\n" +
                    "\n" +
                    "If x is not a number or if base is given, then x must be a string,\n" +
                    "bytes, or bytearray instance representing an integer literal in the\n" +
                    "given base.  The literal can be preceded by '+' or '-' and be surrounded\n" +
                    "by whitespace.  The base defaults to 10.  Valid bases are 0 and 2-36.\n" +
                    "Base 0 means to interpret the base from the string as an integer literal.\n" +
                    "\n" +
                    "  >>> int('0b100', base=0)\n" +
                    "  4\n",
            default_args = {"x", "base"}
    )
    public Int(org.python.Object[] args, java.util.Map<java.lang.String, org.python.Object> kwargs) {
        if (args[0] == null) {
            this.value = 0;
        } else if (args[1] == null) {
            try {
                this.value = ((org.python.types.Int) args[0].__int__()).value;
            } catch (org.python.exceptions.AttributeError ae) {
                if (org.Python.VERSION < 0x03040300) {
                    throw new org.python.exceptions.TypeError(
                            "int() argument must be a string or a number, not '" + args[0].typeName() + "'"
                    );
                } else {
                    throw new org.python.exceptions.TypeError(
                            "int() argument must be a string, a bytes-like object or a number, not '" +
                                    args[0].typeName() + "'"
                    );
                }
            }
        } else if (args.length >= 3) {
            throw new org.python.exceptions.NotImplementedError(java.lang.String.format(
                    "int() takes at most 2 arguments (%d given)",
                    args.length
            ));
        }
    }

    // public org.python.Object __new__() {
    //     throw new org.python.exceptions.NotImplementedError("int.__new__() has not been implemented");
    // }

    // public org.python.Object __init__() {
    //     throw new org.python.exceptions.NotImplementedError("int.__init__() has not been implemented");
    // }

    @org.python.Method(
            __doc__ = "Return repr(self)."
    )
    public org.python.types.Str __repr__() {
        return new org.python.types.Str(java.lang.Long.toString(this.value));
    }

    @org.python.Method(
            __doc__ = ""
    )
    public org.python.types.Str __format__(org.python.Object format_str) {
        throw new org.python.exceptions.NotImplementedError("int.__format__() has not been implemented");
    }

    @org.python.Method(
            __doc__ = ""
    )
    public org.python.types.Str __getitem__(org.python.Object format_str) {
        throw new org.python.exceptions.TypeError("'int' object is not subscriptable");
    }

    @org.python.Method(
            __doc__ = "Return self<value.",
            args = {"other"}
    )
    public org.python.Object __lt__(org.python.Object other) {
        return IntegerOperation.lessThan(this, other);
    }

    @org.python.Method(
            __doc__ = "Return self<=value.",
            args = {"other"}
    )
    public org.python.Object __le__(org.python.Object other) {
        return IntegerOperation.lessThanOrEqual(this, other);
    }

    @org.python.Method(
            __doc__ = "Return self==value.",
            args = {"other"}
    )
    public org.python.Object __eq__(org.python.Object other) {
        return IntegerOperation.equal(this, other);
    }

    @org.python.Method(
            __doc__ = "Return self>value.",
            args = {"other"}
    )
    public org.python.Object __gt__(org.python.Object other) {
        return IntegerOperation.greaterThan(this, other);
    }

    @org.python.Method(
            __doc__ = "Return self>=value.",
            args = {"other"}
    )
    public org.python.Object __ge__(org.python.Object other) {
        return IntegerOperation.greaterThanOrEqual(this, other);
    }

    @org.python.Method(
            __doc__ = "self != 0"
    )
    public org.python.types.Bool __bool__() {
        return new org.python.types.Bool(this.value);
    }

    public boolean __setattr_null(java.lang.String name, org.python.Object value) {
        // Builtin types can't have attributes set on them.
        return false;
    }

    @org.python.Method(
            __doc__ = "__dir__() -> list\ndefault dir() implementation"
    )
    public org.python.types.List __dir__() {
        throw new org.python.exceptions.NotImplementedError("int.__dir__() has not been implemented");
    }

    @org.python.Method(
            __doc__ = "Return self+value.",
            args = {"other"}
    )
    public org.python.Object __add__(org.python.Object other) {
        return IntegerOperation.add(this, other, false);
    }

    @org.python.Method(
            __doc__ = "Return self-value.",
            args = {"other"}
    )
    public org.python.Object __sub__(org.python.Object other) {
        return IntegerOperation.substract(this, other, false);
    }

    @org.python.Method(
            __doc__ = "Return self*value.",
            args = {"other"}
    )
    public org.python.Object __mul__(org.python.Object other) {
        return IntegerOperation.multiply(this, other, false);
    }

    @org.python.Method(
            __doc__ = "Return self/value.",
            args = {"other"}
    )
    public org.python.Object __truediv__(org.python.Object other) {
        return IntegerOperation.floatDivide(this, other, false);
    }

    @org.python.Method(
            __doc__ = "Return self//value.",
            args = {"other"}
    )
    public org.python.Object __floordiv__(org.python.Object other) {
        return IntegerOperation.integerDivide(this, other, false);
    }

    @org.python.Method(
            __doc__ = "Return self%value.",
            args = {"other"}
    )
    public org.python.Object __mod__(org.python.Object other) {
        return IntegerOperation.modulo(this, other, false);
    }

    @org.python.Method(
            __doc__ = "Return divmod(self, value).",
            args = {"other"}
    )
    public org.python.Object __divmod__(org.python.Object other) {
        return IntegerOperation.divmod(this, other);
    }

    @org.python.Method(
            __doc__ = "Return pow(self, other, mod).",
            args = {"other"},
            default_args = {"modulo"}
    )
    public org.python.Object __pow__(org.python.Object other, org.python.Object modulo) {
        return IntegerOperation.power(this, other, modulo);
    }

    @org.python.Method(
            __doc__ = "Return self<<value.",
            args = {"other"}
    )
    public org.python.Object __lshift__(org.python.Object other) {
        return IntegerOperation.shiftLeft(this, other, false);
    }

    @org.python.Method(
            __doc__ = "Return self>>value.",
            args = {"other"}
    )
    public org.python.Object __rshift__(org.python.Object other) {
        return IntegerOperation.shiftRight(this, other, false);
    }

    @org.python.Method(
            __doc__ = "Return self&value.",
            args = {"other"}
    )
    public org.python.Object __and__(org.python.Object other) {
        return IntegerOperation.and(this, other, false);
    }

    @org.python.Method(
            __doc__ = "Return self^value.",
            args = {"other"}
    )
    public org.python.Object __xor__(org.python.Object other) {
        return IntegerOperation.exclusiveOr(this, other, false);
    }

    @org.python.Method(
            __doc__ = "Return self|value.",
            args = {"other"}
    )
    public org.python.Object __or__(org.python.Object other) {
        return IntegerOperation.or(this, other, false);
    }

    @org.python.Method(
            __doc__ = "Return value+self.",
            args = {"other"}
    )
    public org.python.Object __radd__(org.python.Object other) {
        throw new org.python.exceptions.NotImplementedError("int.__radd__() has not been implemented");
    }

    @org.python.Method(
            __doc__ = "Return value-self.",
            args = {"other"}
    )
    public org.python.Object __rsub__(org.python.Object other) {
        throw new org.python.exceptions.NotImplementedError("int.__rsub__() has not been implemented");
    }

    @org.python.Method(
            __doc__ = "Return value*self.",
            args = {"other"}
    )
    public org.python.Object __rmul__(org.python.Object other) {
        throw new org.python.exceptions.NotImplementedError("int.__rmul__() has not been implemented");
    }

    @org.python.Method(
            __doc__ = "Return value/self.",
            args = {"other"}
    )
    public org.python.Object __rtruediv__(org.python.Object other) {
        throw new org.python.exceptions.NotImplementedError("int.__rtruediv__() has not been implemented");
    }

    @org.python.Method(
            __doc__ = "Return value//self.",
            args = {"other"}
    )
    public org.python.Object __rfloordiv__(org.python.Object other) {
        throw new org.python.exceptions.NotImplementedError("int.__rfloordiv__() has not been implemented");
    }

    @org.python.Method(
            __doc__ = "Return value%self.",
            args = {"other"}
    )
    public org.python.Object __rmod__(org.python.Object other) {
        throw new org.python.exceptions.NotImplementedError("int.__rmod__() has not been implemented");
    }

    @org.python.Method(
            __doc__ = "Return divmod(value, self).",
            args = {"other"}
    )
    public org.python.Object __rdivmod__(org.python.Object other) {
        throw new org.python.exceptions.NotImplementedError("int.__rdivmod__() has not been implemented");
    }

    @org.python.Method(
            __doc__ = "Return pow(value, self, mod).",
            args = {"other"}
    )
    public org.python.Object __rpow__(org.python.Object other) {
        throw new org.python.exceptions.NotImplementedError("int.__rpow__() has not been implemented");
    }

    @org.python.Method(
            __doc__ = "Return value<<self.",
            args = {"other"}
    )
    public org.python.Object __rlshift__(org.python.Object other) {
        throw new org.python.exceptions.NotImplementedError("int.__rlshift__() has not been implemented");
    }

    @org.python.Method(
            __doc__ = "Return value>>self.",
            args = {"other"}
    )
    public org.python.Object __rrshift__(org.python.Object other) {
        throw new org.python.exceptions.NotImplementedError("int.__rrshift__() has not been implemented");
    }

    @org.python.Method(
            __doc__ = "Return value&self.",
            args = {"other"}
    )
    public org.python.Object __rand__(org.python.Object other) {
        throw new org.python.exceptions.NotImplementedError("int.__rand__() has not been implemented");
    }

    @org.python.Method(
            __doc__ = "Return value^self.",
            args = {"other"}
    )
    public org.python.Object __rxor__(org.python.Object other) {
        throw new org.python.exceptions.NotImplementedError("int.__rxor__() has not been implemented");
    }

    @org.python.Method(
            __doc__ = "Return value|self.",
            args = {"other"}
    )
    public org.python.Object __ror__(org.python.Object other) {
        throw new org.python.exceptions.NotImplementedError("int.__ror__() has not been implemented");
    }

    @org.python.Method(
            __doc__ = "",
            args = {"other"}
    )
    public org.python.Object __iadd__(org.python.Object other) {
        return IntegerOperation.add(this, other, true);
    }

    @org.python.Method(
            __doc__ = "",
            args = {"other"}
    )
    public org.python.Object __isub__(org.python.Object other) {
        return IntegerOperation.substract(this, other, true);
    }

    @org.python.Method(
            __doc__ = "",
            args = {"other"}
    )
    public org.python.Object __imul__(org.python.Object other) {
        return IntegerOperation.multiply(this, other, true);
    }

    @org.python.Method(
            __doc__ = "",
            args = {"other"}
    )
    public org.python.Object __itruediv__(org.python.Object other) {
        return IntegerOperation.floatDivide(this, other, true);
    }

    @org.python.Method(
            __doc__ = "",
            args = {"other"}
    )
    public org.python.Object __ifloordiv__(org.python.Object other) {
        return IntegerOperation.integerDivide(this, other, true);
    }

    @org.python.Method(
            __doc__ = "",
            args = {"other"}
    )
    public org.python.Object __imod__(org.python.Object other) {
        return IntegerOperation.modulo(this, other, true);
    }

    @org.python.Method(
            __doc__ = "",
            args = {"other"}
    )
    public org.python.Object __ipow__(org.python.Object other) {
        return IntegerOperation.power(this, other, null);
    }

    @org.python.Method(
            __doc__ = "",
            args = {"other"}
    )
    public org.python.Object __ilshift__(org.python.Object other) {
        return IntegerOperation.shiftLeft(this, other, true);
    }

    @org.python.Method(
            __doc__ = "",
            args = {"other"}
    )
    public org.python.Object __irshift__(org.python.Object other) {
        return IntegerOperation.shiftRight(this, other, true);
    }

    @org.python.Method(
            __doc__ = "",
            args = {"other"}
    )
    public org.python.Object __iand__(org.python.Object other) {
        return IntegerOperation.and(this, other, true);
    }

    @org.python.Method(
            __doc__ = "",
            args = {"other"}
    )
    public org.python.Object __ixor__(org.python.Object other) {
        return IntegerOperation.exclusiveOr(this, other, true);
    }

    @org.python.Method(
            __doc__ = "",
            args = {"other"}
    )
    public org.python.Object __ior__(org.python.Object other) {
        return IntegerOperation.or(this, other, true);
    }

    @org.python.Method(
            __doc__ = "-self"
    )
    public org.python.Object __neg__() {
        return new org.python.types.Int(-this.value);
    }

    @org.python.Method(
            __doc__ = "+self"
    )
    public org.python.Object __pos__() {
        return new org.python.types.Int(this.value);
    }

    @org.python.Method(
            __doc__ = "abs(self)"
    )
    public org.python.Object __abs__() {
        return new org.python.types.Int(Math.abs(this.value));
    }

    @org.python.Method(
            __doc__ = "~self"
    )
    public org.python.Object __invert__() {
        return new org.python.types.Int(-(this.value + 1));
    }

    @org.python.Method(
            __doc__ = "int(self)"
    )
    public org.python.Object __int__() {
        return new org.python.types.Int(this.value);
    }

    @org.python.Method(
            __doc__ = "float(self)"
    )
    public org.python.Object __float__() {
        return new org.python.types.Float((float) this.value);
    }

    @org.python.Method(
            __doc__ = "Rounding an Integral returns itself.\nRounding with an ndigits argument also returns an integer."
    )
    public org.python.Object __round__(org.python.Object ndigits) {
        if (ndigits instanceof org.python.types.Int) {
            return new org.python.types.Int(this.value);
        }
        throw new org.python.exceptions.TypeError("'" + ndigits.typeName() + "' object cannot be interpreted as an integer");
    }
}
