package org.python.types;

import org.python.internals.IntegerOperation;

public class Bool extends org.python.types.Object implements org.python.java.Integer {
    public boolean value;   // TODO: Private me!!!!

    public static org.python.Object TRUE = new org.python.types.Bool(true);
    public static org.python.Object FALSE = new org.python.types.Bool(false);

    public long getInteger() {
        return this.value ? 1 : 0;
    }

    public java.lang.Object toJava() {
        return this.value;
    }

    public org.python.Object byValue() {
        return new org.python.types.Bool(this.value);
    }

    public int hashCode() {
        return new java.lang.Boolean(this.value).hashCode();
    }

    public Bool(boolean bool) {
        super();
        this.value = bool;
    }

    public Bool(long value) {
        this(value != 0);
    }

    @org.python.Method(
            __doc__ = "bool(x) -> bool" +
                    "\n" +
                    "Returns True when the argument x is true, False otherwise.\n" +
                    "The builtins True and False are the only two instances of the class bool.\n" +
                    "The class bool is a subclass of the class int, and cannot be subclassed.\n",
            default_args = {"x"}
    )
    public Bool(org.python.Object[] args, java.util.Map<java.lang.String, org.python.Object> kwargs) {
        this(args[0] == null ? false : args[0].toBoolean());
    }
    // public org.python.Object __new__() {
    //     throw new org.python.exceptions.NotImplementedError("bool.__new__() has not been implemented.");
    // }

    // public org.python.Object __init__() {
    //     throw new org.python.exceptions.NotImplementedError("bool.__init__() has not been implemented.");
    // }

    @org.python.Method(
            __doc__ = "Return repr(self)."
    )
    public org.python.types.Str __repr__() {
        return new org.python.types.Str(this.value ? "True" : "False");
    }

    @org.python.Method(
            __doc__ = ""
    )
    public org.python.types.Str __format__(org.python.Object format_string) {
        throw new org.python.exceptions.NotImplementedError("'bool'.__format__ has not been implemented.");
    }

    @org.python.Method(
            __doc__ = ""
    )
    public org.python.types.Str __getitem__(org.python.Object format_string) {
        throw new org.python.exceptions.TypeError("'bool' object is not subscriptable");
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
        return this;
    }

    public boolean __setattr_null(java.lang.String name, org.python.Object value) {
        // Builtin types can't have attributes set on them.
        return false;
    }

    @org.python.Method(
            __doc__ = "__dir__() -> list\ndefault dir() implementation"
    )
    public org.python.types.List __dir__() {
        throw new org.python.exceptions.NotImplementedError("bool.__dir__() has not been implemented.");
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
            __doc__ = "Return pow(self, value, mod).",
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
        throw new org.python.exceptions.NotImplementedError("bool.__radd__() has not been implemented.");
    }

    @org.python.Method(
            __doc__ = "Return value-self.",
            args = {"other"}
    )
    public org.python.Object __rsub__(org.python.Object other) {
        throw new org.python.exceptions.NotImplementedError("bool.__rsub__() has not been implemented.");
    }

    @org.python.Method(
            __doc__ = "Return value*self.",
            args = {"other"}
    )
    public org.python.Object __rmul__(org.python.Object other) {
        throw new org.python.exceptions.NotImplementedError("bool.__rmul__() has not been implemented.");
    }

    @org.python.Method(
            __doc__ = "Return value/self.",
            args = {"other"}
    )
    public org.python.Object __rtruediv__(org.python.Object other) {
        throw new org.python.exceptions.NotImplementedError("bool.__rtruediv__() has not been implemented.");
    }

    @org.python.Method(
            __doc__ = "Return value//self.",
            args = {"other"}
    )
    public org.python.Object __rfloordiv__(org.python.Object other) {
        throw new org.python.exceptions.NotImplementedError("bool.__rfloordiv__() has not been implemented.");
    }

    @org.python.Method(
            __doc__ = "Return value%self.",
            args = {"other"}
    )
    public org.python.Object __rmod__(org.python.Object other) {
        throw new org.python.exceptions.NotImplementedError("bool.__rmod__() has not been implemented.");
    }

    @org.python.Method(
            __doc__ = "Return divmod(value, self).",
            args = {"other"}
    )
    public org.python.Object __rdivmod__(org.python.Object other) {
        throw new org.python.exceptions.NotImplementedError("bool.__rdivmod__() has not been implemented.");
    }

    @org.python.Method(
            __doc__ = "Return pow(value, self, mod).",
            args = {"other"}
    )
    public org.python.Object __rpow__(org.python.Object other) {
        throw new org.python.exceptions.NotImplementedError("bool.__rpow__() has not been implemented.");
    }

    @org.python.Method(
            __doc__ = "Return value<<self.",
            args = {"other"}
    )
    public org.python.Object __rlshift__(org.python.Object other) {
        throw new org.python.exceptions.NotImplementedError("bool.__rlshift__() has not been implemented.");
    }

    @org.python.Method(
            __doc__ = "Return value>>self.",
            args = {"other"}
    )
    public org.python.Object __rrshift__(org.python.Object other) {
        throw new org.python.exceptions.NotImplementedError("bool.__rrshift__() has not been implemented.");
    }

    @org.python.Method(
            __doc__ = "Return value&self.",
            args = {"other"}
    )
    public org.python.Object __rand__(org.python.Object other) {
        throw new org.python.exceptions.NotImplementedError("bool.__rand__() has not been implemented.");
    }

    @org.python.Method(
            __doc__ = "Return value^self.",
            args = {"other"}
    )
    public org.python.Object __rxor__(org.python.Object other) {
        throw new org.python.exceptions.NotImplementedError("bool.__rxor__() has not been implemented.");
    }

    @org.python.Method(
            __doc__ = "Return value|self.",
            args = {"other"}
    )
    public org.python.Object __ror__(org.python.Object other) {
        throw new org.python.exceptions.NotImplementedError("bool.__ror__() has not been implemented.");
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
        return new org.python.types.Int(this.value ? -1 : 0);
    }

    @org.python.Method(
            __doc__ = "+self"
    )
    public org.python.Object __pos__() {
        return new org.python.types.Int(this.value ? 1 : 0);
    }

    @org.python.Method(
            __doc__ = "abs(self)"
    )
    public org.python.Object __abs__() {
        return new org.python.types.Int(this.value ? 1 : 0);
    }

    @org.python.Method(
            __doc__ = "~self"
    )
    public org.python.Object __invert__() {
        return new org.python.types.Int(this.value ? -2 : -1);
    }

    @org.python.Method(
            __doc__ = "int(self)"
    )
    public org.python.types.Int __int__() {
        return new org.python.types.Int(this.value ? 1 : 0);
    }

    @org.python.Method(
            __doc__ = "float(self)"
    )
    public org.python.types.Float __float__() {
        return new org.python.types.Float(this.value ? 1 : 0);
    }

    @org.python.Method(
            __doc__ = "Rounding an Integral returns itself.\nRounding with an ndigits argument also returns an integer."
    )
    public org.python.Object __round__(org.python.Object ndigits) {
        if (ndigits instanceof org.python.types.Int) {
            return new org.python.types.Int(this.value ? 1 : 0);
        }
        throw new org.python.exceptions.TypeError("'" + ndigits.typeName() + "' object cannot be interpreted as an integer");
    }

    @org.python.Method(
            __doc__ = "Return self converted to an integer, if self is suitable for use as an index into a list."
    )
    public org.python.Object __index__() {
        return new org.python.types.Int(this.value ? 1 : 0);
    }
}
