package org.python.internals;

public class IntegerOperation {
    /**
     * Helper to compare the two values. The second argument MUST be a org.python.java.Integer instance.
     */
    private static int compare(org.python.java.Integer x, org.python.Object y) {
        return java.lang.Long.compare(x.getInteger(), ((org.python.java.Integer) y).getInteger());
    }

    public static org.python.Object lessThan(org.python.java.Integer x, org.python.Object y) {
        if (y instanceof org.python.java.Integer) {
            return new org.python.types.Bool(compare(x, y) < 0);
        }
        return org.python.types.NotImplementedType.NOT_IMPLEMENTED;
    }

    public static org.python.Object lessThanOrEqual(org.python.java.Integer x, org.python.Object y) {
        if (y instanceof org.python.java.Integer) {
            return new org.python.types.Bool(compare(x, y) <= 0);
        }
        return org.python.types.NotImplementedType.NOT_IMPLEMENTED;
    }

    public static org.python.Object greaterThan(org.python.java.Integer x, org.python.Object y) {
        if (y instanceof org.python.java.Integer) {
            return new org.python.types.Bool(compare(x, y) > 0);
        }
        return org.python.types.NotImplementedType.NOT_IMPLEMENTED;
    }

    public static org.python.Object greaterThanOrEqual(org.python.java.Integer x, org.python.Object y) {
        if (y instanceof org.python.java.Integer) {
            return new org.python.types.Bool(compare(x, y) >= 0);
        }
        return org.python.types.NotImplementedType.NOT_IMPLEMENTED;
    }

    public static org.python.Object equal(org.python.java.Integer x, org.python.Object y) {
        if (y instanceof org.python.java.Integer) {
            return new org.python.types.Bool(compare(x, y) == 0);
        }
        return org.python.types.NotImplementedType.NOT_IMPLEMENTED;
    }

    public static org.python.Object add(org.python.java.Integer x, org.python.Object y, boolean inplace) {
        if (y instanceof org.python.java.Integer) {
            return new org.python.types.Int(x.getInteger() + ((org.python.java.Integer) y).getInteger());
        } else if (y instanceof org.python.types.Float) {
            return new org.python.types.Float(x.getInteger() + ((org.python.types.Float) y).value);
        } else if (y instanceof org.python.types.Complex) {
            org.python.types.Complex complex = (org.python.types.Complex) y;
            return new org.python.types.Complex(x.getInteger() + complex.real.value, complex.imag.value);
        }
        throw new org.python.exceptions.TypeError(java.lang.String.format(
                "unsupported operand type(s) for %s: '%s' and '%s'",
                inplace ? "+=" : "+", x.typeName(), y.typeName()
        ));
    }

    public static org.python.Object substract(org.python.java.Integer x, org.python.Object y, boolean inplace) {
        if (y instanceof org.python.java.Integer) {
            return new org.python.types.Int(x.getInteger() - ((org.python.java.Integer) y).getInteger());
        } else if (y instanceof org.python.types.Float) {
            return new org.python.types.Float(x.getInteger() - ((org.python.types.Float) y).value);
        } else if (y instanceof org.python.types.Complex) {
            org.python.types.Complex complex = (org.python.types.Complex) y;
            return new org.python.types.Complex(x.getInteger() - complex.real.value, 0.0 - complex.imag.value);
        }
        throw new org.python.exceptions.TypeError(java.lang.String.format(
                "unsupported operand type(s) for -: '%s' and '%s'",
                inplace ? "+=" : "+", x.typeName(), y.typeName()
        ));
    }

    public static org.python.Object multiply(org.python.java.Integer x, org.python.Object y, boolean inplace) {
        if (y instanceof org.python.java.Integer) {
            return new org.python.types.Int(x.getInteger() * ((org.python.java.Integer) y).getInteger());
        } else if (y instanceof org.python.types.Float) {
            return new org.python.types.Float(x.getInteger() * (((org.python.types.Float) y).value));
        } else if (y instanceof org.python.types.Complex) {
            // Commutative Law!
            return ((org.python.types.Complex) y).__mul__(x);
        } else if (y instanceof org.python.java.ByteArray ||
                   y instanceof org.python.types.Str ||
                   y instanceof org.python.types.List ||
                   y instanceof org.python.types.Tuple) {
            // TODO: Does this mean we should have a Sequence.__mul__()?
            return y.__mul__(x);
        }
        throw new org.python.exceptions.TypeError(java.lang.String.format(
                "unsupported operand type(s) for %s: '%s' and '%s'",
                inplace ? "*=" : "*", x.typeName(), y.typeName()
        ));
    }

    public static org.python.Object floatDivide(org.python.java.Integer x, org.python.Object y, boolean inplace) {
        if (y instanceof org.python.java.Integer) {
            long value = ((org.python.java.Integer) y).getInteger();
            if (value == 0) {
                throw new org.python.exceptions.ZeroDivisionError("division by zero");
            }
            return new org.python.types.Float(x.getInteger() / ((double) value));
        } else if (y instanceof org.python.types.Float) {
            double value = ((org.python.types.Float) y).value;
            if (value == 0.0) {
                throw new org.python.exceptions.ZeroDivisionError("float division by zero");
            }
            return new org.python.types.Float(x.getInteger() / value);
        } else if (y instanceof org.python.types.Complex) {
            return new org.python.types.Complex(x.getInteger(), 0.0).__truediv__(y);
        }
        throw new org.python.exceptions.TypeError(java.lang.String.format(
                "unsupported operand type(s) for %s: '%s' and '%s'",
                inplace ? "/=" : "/", x.typeName(), y.typeName()
        ));
    }

    /**
     * Helper to extract integer from org.python.java.Integer instance.
     * This is used by floor division and modulo methods to check for argument
     * vallidness.
     */
    private static long getNonZeroInteger(org.python.Object y) {
        long value = ((org.python.java.Integer) y).getInteger();
        if (value == 0) {
            throw new org.python.exceptions.ZeroDivisionError("integer division or modulo by zero");
        }
        return value;
    }

    /**
     * Helper to extract floating number from org.python.types.Float instance.
     * This is used by floor division and modulo methods to check for argument
     * vallidness.
     */
    private static double getNonZeroFloat(org.python.Object y, java.lang.String operationName) {
        double value = ((org.python.types.Float) y).value;
        if (value == 0.0) {
            throw new org.python.exceptions.ZeroDivisionError("float " + operationName);
        }
        return value;
    }

    public static org.python.Object integerDivide(org.python.java.Integer x, org.python.Object y, boolean inplace) {
        if (y instanceof org.python.java.Integer) {
            return new org.python.types.Int((long) Math.floor((double) x.getInteger() / getNonZeroInteger(y)));
        } else if (y instanceof org.python.types.Float) {
            return new org.python.types.Float(Math.floor(x.getInteger() / getNonZeroFloat(y, "divmod()")));
        } else if (y instanceof org.python.types.Complex) {
            throw new org.python.exceptions.TypeError("can't take floor of complex number.");
        }
        throw new org.python.exceptions.TypeError(java.lang.String.format(
                "unsupported operand type(s) for %s: '%s' and '%s'",
                inplace ? "//=" : "//", x.typeName(), y.typeName()
        ));
    }

    public static org.python.Object modulo(org.python.java.Integer x, org.python.Object y, boolean inplace) {
        if (y instanceof org.python.java.Integer) {
            long value = x.getInteger();
            long result = Math.floorMod(value, getNonZeroInteger(y));
            // Mimic a CPython bug in Python < 3.6:
            // False % value is False, not 0; True % value returns True if the result should be 1.
            // All other combinations returns an integer (or raises an error).
            // TODO: Is there a reference in what version this was fixed?
            if (org.Python.VERSION < 0x03060000 && x instanceof org.python.types.Bool && (value == 0 || result == 1)) {
                return new org.python.types.Bool(result != 0);
            }
            return new org.python.types.Int(result);
        } else if (y instanceof org.python.types.Float) {
            double value = getNonZeroFloat(y, "modulo");
            double result = (double) x.getInteger() % value;
            if (value > 0.0 && result < 0.0) {
                // second operand is positive, ensure that result is positive
                result += value;
            } else if (value < 0.0 && result > 0.0) {
                // second operand is negative, ensure that result is negative
                result += value; // subtract value, which is negative
            }
            // edge case where operands are 0 and -0.0:
            // need to force sign to negative as adding -0.0 to 0.0 doesn't yield the expected -0.0
            if (value < 0.0 && result >= 0.0) {
                result *= -1;
            }
            return new org.python.types.Float(result);
        } else if (y instanceof org.python.types.Complex) {
            throw new org.python.exceptions.TypeError("can't mod complex numbers.");
        }
        throw new org.python.exceptions.TypeError(java.lang.String.format(
                "unsupported operand type(s) for %s: '%s' and '%s'",
                inplace ? "%=" : "%", x.typeName(), y.typeName()
        ));
    }

    public static org.python.Object divmod(org.python.java.Integer x, org.python.Object y) {
        java.util.List<org.python.Object> data = new java.util.ArrayList<org.python.Object>();
        try {
            data.add(integerDivide(x, y, false));
            data.add(modulo(x, y, false));
        } catch (org.python.exceptions.TypeError te) {
            throw new org.python.exceptions.TypeError(java.lang.String.format(
                    "unsupported operand type(s) for divmod: '%s' and '%s'",
                    x.typeName(), y.typeName()
            ));
        }
        return new org.python.types.Tuple(data);
    }

    /**
     * Helper to extract integer from org.python.java.Integer instance.
     * This is used by bit shift operations to check for argument vallidness.
     */
    private static long getNonNegativeInteger(org.python.Object y) {
        long value = ((org.python.java.Integer) y).getInteger();
        if (value < 0) {
            throw new org.python.exceptions.ValueError("negative shift count");
        }
        return value;
    }

    public static org.python.Object shiftLeft(org.python.java.Integer x, org.python.Object y, boolean inplace) {
        if (y instanceof org.python.java.Integer) {
            return new org.python.types.Int(x.getInteger() << getNonNegativeInteger(y));
        }
        throw new org.python.exceptions.TypeError(java.lang.String.format(
                "unsupported operand type(s) for %s: '%s' and '%s'",
                inplace ? "<<=" : "<<", x.typeName(), y.typeName()
        ));
    }

    public static org.python.Object shiftRight(org.python.java.Integer x, org.python.Object y, boolean inplace) {
        if (y instanceof org.python.java.Integer) {
            return new org.python.types.Int(x.getInteger() >> getNonNegativeInteger(y));
        }
        throw new org.python.exceptions.TypeError(java.lang.String.format(
                "unsupported operand type(s) for %s: '%s' and '%s'",
                inplace ? ">>=" : ">>", x.typeName(), y.typeName()
        ));
    }

    public static org.python.Object and(org.python.java.Integer x, org.python.Object y, boolean inplace) {
        if (y instanceof org.python.java.Integer) {
            long result = x.getInteger() & ((org.python.java.Integer) y).getInteger();
            if (x instanceof org.python.types.Bool && y instanceof org.python.types.Bool) {
                return new org.python.types.Bool(result != 0);
            }
            return new org.python.types.Int(result);
        }
        throw new org.python.exceptions.TypeError(java.lang.String.format(
                "unsupported operand type(s) for &: '%s' and '%s'",
                inplace ? "&=" : "&", x.typeName(), y.typeName()
        ));
    }

    public static org.python.Object or(org.python.java.Integer x, org.python.Object y, boolean inplace) {
        if (y instanceof org.python.java.Integer) {
            long result = x.getInteger() | ((org.python.java.Integer) y).getInteger();
            if (x instanceof org.python.types.Bool && y instanceof org.python.types.Bool) {
                return new org.python.types.Bool(result != 0);
            }
            return new org.python.types.Int(result);
        }
        throw new org.python.exceptions.TypeError(java.lang.String.format(
                "unsupported operand type(s) for |: '%s' and '%s'",
                inplace ? "|=" : "|", x.typeName(), y.typeName()
        ));
    }

    public static org.python.Object exclusiveOr(org.python.java.Integer x, org.python.Object y, boolean inplace) {
        if (y instanceof org.python.java.Integer) {
            long result = x.getInteger() ^ ((org.python.java.Integer) y).getInteger();
            if (x instanceof org.python.types.Bool && y instanceof org.python.types.Bool) {
                return new org.python.types.Bool(result != 0);
            }
            return new org.python.types.Int(result);
        }
        throw new org.python.exceptions.TypeError(java.lang.String.format(
                "unsupported operand type(s) for ^: '%s' and '%s'",
                inplace ? "^=" : "^", x.typeName(), y.typeName()
        ));
    }

    /**
     * Implement power() without modulo argument.
     */
    private static org.python.Object powerHelper(org.python.java.Integer x, org.python.Object y) {
        long xValue = x.getInteger();
        if (y instanceof org.python.java.Integer) {
            long yValue = ((org.python.java.Integer) y).getInteger();
            if (yValue < 0) {
                if (xValue == 0) {
                    throw new org.python.exceptions.ZeroDivisionError("0.0 cannot be raised to a negative power");
                }
                double result = 1.0;
                for (long count = 0; count < -yValue; count++) {
                    result *= xValue;
                }
                return new org.python.types.Float(1.0 / result);
            } else {
                long result = 1;
                for (long count = 0; count < yValue; count++) {
                    result *= xValue;
                }
                return new org.python.types.Int(result);
            }
        } else if (y instanceof org.python.types.Float) {
            double yValue = ((org.python.types.Float) y).value;
            if (xValue == 0 && yValue < 0.0) {
                throw new org.python.exceptions.ZeroDivisionError("0.0 cannot be raised to a negative power");
            }
            if (xValue < 0 && Math.floor(yValue) != yValue) {
                // The result will be complex, so make it a complex type instead
                return (new org.python.types.Complex(xValue, 0)).__pow__(y, null);
            }
            return new org.python.types.Float(java.lang.Math.pow((double) xValue, yValue));
        } else if (y instanceof org.python.types.Complex) {
            org.python.types.Complex cmplx_obj = new org.python.types.Complex(xValue, 0.0);
            org.python.types.Complex other_cmplx_obj = (org.python.types.Complex) y;
            return cmplx_obj.__pow__(other_cmplx_obj, null);
        }
        throw new org.python.exceptions.TypeError(String.format(
                "unsupported operand type(s) for ** or pow(): '%s' and '%s'",
                x.typeName(), y.typeName()
        ));
    }

    /**
     * Implement power() with modulo argument.
     */
    private static org.python.Object powerHelper(org.python.java.Integer x, org.python.Object y, org.python.Object modulo) {
        /* if exponent is not int and modulo specified raise TypeError*/
        if (y instanceof org.python.types.Float) {
            throw new org.python.exceptions.TypeError("pow() 3rd argument not allowed unless all arguments are integers");
        }

        if (y instanceof org.python.java.Integer) {
            long xValue = x.getInteger();
            long yValue = ((org.python.java.Integer) y).getInteger();
            long mValue = ((org.python.types.Int) modulo).value;
            /* if exponent is negative raise TypeError*/
            if (yValue < 0) {
                if (org.Python.VERSION < 0x03050000) {
                    throw new org.python.exceptions.TypeError(
                            "pow() 2nd argument cannot be negative when 3rd argument specified"
                    );
                } else {
                    throw new org.python.exceptions.ValueError(
                            "pow() 2nd argument cannot be negative when 3rd argument specified"
                    );
                }
            }
            /* if modulus == 0: raise ValueError() */
            if (mValue == 0) {
                throw new org.python.exceptions.ValueError("pow() 3rd argument cannot be 0");
            }
            /* if modulus == 1:
                   return 0 */
            if (mValue == 1) {
                return new org.python.types.Int(0);
            }
            /* if base < 0:
                   base = base % modulus */
            if (xValue < 0) {
                xValue = xValue % mValue;
            }
            /* At this point a, b, and c are guaranteed non-negative UNLESS
            c is NULL, in which case a may be negative. */
            long result = 1;
            xValue = xValue % mValue;
            while (yValue != 0) {
                if (yValue % 2 == 1) {
                    result = (result * xValue) % mValue;
                }
                xValue = (xValue * xValue) % mValue;
                yValue /= 2;
            }
            return new org.python.types.Int(result);
        }
        throw new org.python.exceptions.TypeError(String.format(
                "unsupported operand type(s) for ** or pow(): '%s' and '%s'",
                x.typeName(), y.typeName()
        ));
    }

    public static org.python.Object power(org.python.java.Integer x, org.python.Object y, org.python.Object modulo) {
        return modulo == null ? powerHelper(x, y) : powerHelper(x, y, modulo);
    }
}
