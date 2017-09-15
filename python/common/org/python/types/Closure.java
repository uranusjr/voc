package org.python.types;

public class Closure extends org.python.types.Object {
    public java.util.Map<java.lang.String, org.python.Object> closure_vars;

    public Closure(java.util.Map<java.lang.String, org.python.Object> vars) {
        super();
        this.closure_vars = vars;
    }

    @org.python.Method(
            __doc__ = "Return repr(self)."
    )
    public org.python.Object __repr__() {
        return new org.python.types.Str(String.format("<function %s at 0x%x>", this.typeName(), this.hashCode()));
    }
}
