package org.python.internals;

public final class StringFormatter {

    /*############################# =- Public -= #############################*/
    /*--- Public class methods  ---------------=------------------------------*/

    /**
     * This is the main interface that enables that basically any Python object
     * to be parsed into a given format String. If a Tuple is passed it serves
     * as argument list for format specifiers in the String. A dict requires
     * <b>all</b> format specifier to be mapped to a Key. The dict can only
     * make use if values that have a key of String type since no type cast
     * is performed with the key obtained from the format String.
     *
     * @param  formatString                        Any Python String.
     * @param  arg                                 Any Python objct. Passing
     *                                             {@link org.python.types.Dict Dict} or
     *                                             {@link org.python.types.Tuple Tuple}
     *                                             has special meaning.
     *
     * @return                                      A new String that where any specifier in the format
     *                                                string has been replaced by a corresponding value
     *                                                in args.
     *
     * @throws org.python.exceptions.TypeError     Various errors. Seriously, almost anything that could go wrong throws TypeError.
     * @throws org.python.exceptions.KeyError      if a predicted key could not be found in the given kwargs dict.
     * @throws org.python.exceptions.OverflowError if a character conversion exceeds unicode range.
     * @throws org.python.exceptions.ValueError    if a conversion character is unknown. See Python's documentation.
     * @throws java.lang.NullPointerException      if null passed to method in any argument.
     */
    public static org.python.types.Str format(
            org.python.types.Str formatString, org.python.Object arg)
            throws
                org.python.exceptions.TypeError, org.python.exceptions.KeyError,
                org.python.exceptions.OverflowError, org.python.exceptions.ValueError,
                java.lang.NullPointerException {
        if (formatString == null || arg == null) {
            throw new java.lang.NullPointerException("Cannot pass any null reference to this method.");
        }

        if (arg instanceof org.python.types.Tuple) {
            return new StringFormatter(formatString.value, ((org.python.types.Tuple) arg).value)._format();
        } else if (arg instanceof org.python.types.Dict) {
            return new StringFormatter(formatString.value, ((org.python.types.Dict) arg).value)._format();
        } else {
            return new StringFormatter(formatString.value, arg)._format();
        }
    }

    /**
     * For testing purposes.
     * @param  str The format String.
     * @param  arg Any Python object.
     * @return     The formatted string.
     */
    protected static java.lang.String format(java.lang.String str, org.python.Object arg) {
        return format(new org.python.types.Str(str), arg).value;
    }

    /*############################# =- Private -= ############################*/
    /*--- Private constructor  -----------------------------------------------*/
    private StringFormatter(java.lang.String formatString, java.util.List<org.python.Object> args) {
        this.formatString = formatString;
        this.args = args;
        this.kwargs = null;

        fillCharacterQueue();
    }

    private StringFormatter(java.lang.String formatString, java.util.Map<org.python.Object, org.python.Object> kwargs) {
        this.formatString = formatString;
        this.args = new java.util.LinkedList<>();
        this.kwargs = kwargs;

        // necessary for certain edge cases
        this.args.add(new org.python.types.Dict(kwargs));
        fillCharacterQueue();
    }

    private StringFormatter(java.lang.String formatString, org.python.Object arg) {
        this.formatString = formatString;
        this.args = new java.util.LinkedList<>();
        this.kwargs = null;

        this.singleValueIsAllowed = arg instanceof org.python.types.Bytes
                                 || arg instanceof org.python.types.ByteArray
                                 || arg instanceof org.python.types.Range
                                 || arg instanceof org.python.types.List;

        this.args.add(arg);
        fillCharacterQueue();
    }

    /*--- Private object methods  --------------------------------------------*/

    /**
     * Parses a key if the % char is immediately followed by an opening bracket.
     * It then parses that key with regard to correct bracket count (the key ist
     * always of type string, nothing will be cast and '(' and ')' can be part
     * of it).
     *
     * If a key could be parsed it is looked up in a dictionary that was provided
     * and inserted into the arg list so the following code will not be able to
     * tell the difference.
     */
    private void insertValueOfKeyAsArgIntoArgs() {
        if (!isHeadOfQueueAndIfSoRemove('(')) {
            return;
        }

        handleMappingExceptions();

        java.lang.StringBuilder keyBuilder = new java.lang.StringBuilder();
        for (int openBrackets = 1; openBrackets > 0;) {
            if (characterQueue.isEmpty()) {
                throw new org.python.exceptions.ValueError("incomplete format key");
            }

            if (isHeadOfQueueAndIfSoRemove('(')) {
                openBrackets++;
            } else if (isHeadOfQueueAndIfSoRemove(')') && openBrackets == 1) {
                openBrackets--;
            } else {
                keyBuilder.append(pollNextCharacter());
            }
        }

        org.python.types.Str key = new org.python.types.Str(keyBuilder.toString());

        if (!kwargs.containsKey(key)) {
            throw new org.python.exceptions.KeyError(key);
        }

        // This is really not intuitive. But apparently Python behaves
        // similar. Consider the following valid python code:
        //
        //      %(C)*%s" % {'C': 234, 'D': 2}
        //
        // The * is evaluated as we know it. eval() of the expression
        // yields "%s" instead of raising an error due to the missing
        // arglist as one would expect. Instead if the value for key 'C'
        // is of type str "TypeError: * wants int" will be raised.
        // Anyway, this works.
        args.add(currentArgumentIndex, kwargs.get(key));
    }

    private java.util.Map<java.lang.Character, java.lang.Boolean> parseConversionFlags() {

        java.util.Map<java.lang.Character, java.lang.Boolean> flags = getInitialConversionFlags();

        while (flags.containsKey(peekCharacterQueue())) {
            switch (pollNextCharacter()) {
                case '#':
                    flags.put('#', true);
                    break;

                case '0':
                    // '-' overrides '0'.
                    if (!flags.get('-')) {
                        flags.put('0', true);
                    }
                    break;

                case '-':
                    flags.put('-', true);
                    flags.put('0', false);
                    break;

                case ' ':
                    // '+' overrides ' '
                    if (!flags.get('+')) {
                        flags.put(' ', true);
                    }
                    break;

                case '+':
                    flags.put('+', true);
                    flags.put(' ', false);
                    break;

                default:
                    /* this isn't a python error. I'm just throwing an exception to the
                    caller that the conversion flag isn't legal. */
                    throw new org.python.exceptions.TypeError("illegal character");
            }
        }

        return flags;
    }

    /**
     * The minimum width the arg to be formatted should fill out.
     * @return the minimum width.
     */
    private java.lang.Long parseMinimumWidth() {
        if (isHeadOfQueueAndIfSoRemove('*')) {
            return getIntValueForStarInFormatSpecification();
        }

        java.lang.Long minimumWidth = DEFAULT_MINIMUM_WIDTH; /* == 0 */
        while (java.lang.Character.isDigit(peekCharacterQueue())) {
            minimumWidth = (10 * minimumWidth) + pollNextCharacter() - '0';
        }

        return minimumWidth;
    }

    /**
     * Determine the precision of the arg. In Python this means truncating
     * for a string, precision as usual for floats and missing digits of int
     * values will be filled with zeros.
     *
     * @return The correct precision of a default value.
     */
    private java.lang.Long parsePrecision() {
        java.lang.Long precision = PRECSION_NOT_SET;

        if (isHeadOfQueueAndIfSoRemove('.')) {
            if (isHeadOfQueueAndIfSoRemove('*')) {
                return getIntValueForStarInFormatSpecification();
            }

            /* Python accepts the precision to be omitted and just assumes
            it to be 0. Java would throw UnknownFormatConversionException.*/
            precision = 0L;
            while (java.lang.Character.isDigit(peekCharacterQueue())) {
                precision = (10 * precision) + pollNextCharacter() - '0';
            }
        }

        return precision;
    }


    /**
     * How may character of the format String have already been processed.
     * @return Number of processed characters.
     */
    private int getCurrentCharacterIndex() {
        return formatString.length() - characterQueue.size() - 1;
    }


    /**
     * Determine the conversion. It is important to note here
     * that the wanted conversion type and the object's type do not have to
     * match. In most cases that is not a problem (e.g. if conversion char
     * is d and value has float type it's just type casted). In some cases
     * it leads to certain errors (e.g. d and "zany" even Python won't
     * handle). Furthermore does Java behave very differently from Python,
     * therefore two things are due here:
     *
     *     1. throw errors as needed for Python.
     *     2. then convert most types so Java won't throw errors at us.
     *
     * The general pattern with Java and Python regarding Exceptions is
     * that while Python either ignores options that make no sense with
     * some conversion, Java throws an Exception for everything that does
     * not match.
     *
     *
     * @param conversionFlags The flags '#', '-', '+', ' ', '0',
     * @param minimumWidth    The minimum width of the formatted string
     * @param precision       Precision for float and int values. Can truncate
     *                        strings.
     */
    private void parseConversionChar(
            java.util.Map<java.lang.Character, java.lang.Boolean> conversionFlags,
            java.lang.Long minimumWidth, java.lang.Long precision) {

        char conversionChar = pollNextCharacter();

        /*  We just ignore everything in between %...% just
        like Python does it. No kidding. */
        if (conversionChar == '%') {
            buffer.append('%');
            return;
        }

        org.python.Object fmtObjectPython = popNextArg();
        pythonObjectMatchesConversionOrDie(fmtObjectPython, conversionChar);

        // Choose the Java Object representation and remove and store the
        // sign if the Object represents a number.
        java.lang.Object fmtObjectJava;
        char sign = SIGN_UNDEFINED;
        if (conversionChar == 's') {
            fmtObjectJava = fmtObjectPython.__str__().toJava();
        } else if (conversionChar == 'r') {
            fmtObjectJava = fmtObjectPython.__repr__().toJava();
        } else if (fmtObjectPython instanceof org.python.types.Int) {
            sign = 0 < (java.lang.Long) fmtObjectPython.toJava() ? SIGN_POSITIV : SIGN_NEGATIV;
            fmtObjectJava = java.lang.Math.abs((java.lang.Long) fmtObjectPython.toJava());
        } else if (fmtObjectPython instanceof org.python.types.Float) {
            sign = 0 < (java.lang.Double) fmtObjectPython.toJava() ? SIGN_POSITIV : SIGN_NEGATIV;
            fmtObjectJava = java.lang.Math.abs((java.lang.Double) fmtObjectPython.toJava());
        } else if (fmtObjectPython instanceof org.python.types.Bool /* && not conv == s or r */) {
            fmtObjectJava = ((org.python.types.Bool) fmtObjectPython).value ? 1 : 0;
        } else {
            /* Again not a Python error. */
            throw new org.python.exceptions.TypeError("Conversion impossible");
        }

        java.lang.String preFormattedObject;
        if ("gGfFeE".indexOf(conversionChar) != -1) {
            java.lang.Double value = ((java.lang.Number) fmtObjectJava).doubleValue();
            java.lang.Boolean isVeryLargeOrVerySmall = value >= java.lang.Math.pow(10, precision) || value < .0001;

            if ("gGeE".indexOf(conversionChar) != -1 && isVeryLargeOrVerySmall) {
                preFormattedObject = toExp(value, precision, conversionChar, conversionFlags.get('#'));
            } else {
                preFormattedObject = toFixed(value, precision, conversionFlags.get('#'));
            }

        } else if (conversionChar == 'c') {
            preFormattedObject = java.lang.String.format(
                "%" + conversionChar, (java.lang.Character) fmtObjectJava);

        } else if ("iudoxX".indexOf(conversionChar) != -1) {
            // resolve alias
            if ("iu".indexOf(conversionChar) != -1) {
                conversionChar = 'd';
            }

            preFormattedObject = toInteger(fmtObjectJava, precision, conversionChar);

        } else if ("sr".indexOf(conversionChar) != -1) {
            preFormattedObject = java.lang.String.format(
                "%"
                + (precision != PRECSION_NOT_SET ? "." + precision : "")
                + conversionChar,
                fmtObjectJava
            );

        } else {
            throw new org.python.exceptions.ValueError(
                String.format("unsupported format character '%c' (%#x) at index %d",
                    conversionChar, (int) conversionChar, getCurrentCharacterIndex()));
        }

        java.lang.String signToUse = determineSignToUse(sign, conversionFlags);

        java.lang.String alternateIntegerFormPrefix;
        if (conversionFlags.get('#') && "oxX".indexOf(conversionChar) != -1) {
            alternateIntegerFormPrefix = "0" + conversionChar;
        } else {
            alternateIntegerFormPrefix = "";
        }

        char paddingChar = conversionFlags.get('0') ? '0' : ' ';
        long missingStringLength = java.lang.Math.max(0L, minimumWidth - preFormattedObject.length() - signToUse.length());
        java.lang.String padding = getPaddingOfLength(paddingChar, missingStringLength);

        if (conversionFlags.get('-')) {
            buffer.append(signToUse);
            buffer.append(alternateIntegerFormPrefix);
            buffer.append(preFormattedObject);
            buffer.append(padding);
        } else {
            buffer.append(padding);
            buffer.append(signToUse);
            buffer.append(alternateIntegerFormPrefix);
            buffer.append(preFormattedObject);
        }
    }

    /**
     * The beginning of a formatting specification. Everything is processed as
     * the steps below suggest.
     */
    private void processConversion() {
        insertValueOfKeyAsArgIntoArgs();
        java.util.Map<java.lang.Character, java.lang.Boolean> conversionFlags = parseConversionFlags();

        long minimumWidth = parseMinimumWidth();
        long precision = parsePrecision();

        // Java does not know these and Python does not care.
        if ("hlL".indexOf(peekCharacterQueue()) != -1) {
            pollNextCharacter();
        }

        parseConversionChar(conversionFlags, minimumWidth, precision);
    }


    /**
     * Iterates the format string exactly once without looking ahead. The
     * implementation loosely follows the original CPython format string
     * implementation. Instead of a buffer the implementation uses a
     * {@link java.lang.StringBuilder StringBuilder} to concatenate the string.
     * There is room for optimization though since some Strings are allocated
     * nonetheless. StringBuilder dynamically expanded it's buffer
     * automatically.
     *
     * The detection of errors is lazy, meaning that no a priori checks are
     * preformed (e.g. looking for '%(' and assert a map is given). A string may
     * be parsed almost until the end until an error is thrown. This implements
     * Python behaviour.
     *
     * @return The Python printf style formatted string.
     */
    private org.python.types.Str _format() {
        while (!characterQueue.isEmpty()) {
            if (peekCharacterQueue() != '%') {
                buffer.append(pollNextCharacter());
            } else {
                pollNextCharacter();
                processConversion();
            }
        }

        ensureNoArgumentsAreLeft();
        return new org.python.types.Str(buffer.toString());
    }

    /**
     * Pops and casts the next value for the argument list for a starred
     * expression.
     *
     * @return The Integer value for the next argument of the arg list.
     */
    private java.lang.Long getIntValueForStarInFormatSpecification() {
        if (peekNextArg() instanceof org.python.types.Int) {
            return ((org.python.types.Int) popNextArg()).value;
        } else if (peekNextArg() instanceof org.python.types.Bool) {
            return ((org.python.types.Bool) popNextArg()).value ? 1L : 0L;
        } else {
            throw new org.python.exceptions.TypeError("* wants int");
        }
    }

    /**
     * Fills the character Queue with the current format String.
     */
    private void fillCharacterQueue() {
        for (char c : formatString.toCharArray()) {
            characterQueue.add(c);
        }
    }

    /**
     * Wrapper around peeking at the Character Queue.
     * @return [description]
     */
    private java.lang.Character peekCharacterQueue() {
        if (characterQueue.isEmpty()) {
            throw new org.python.exceptions.ValueError("incomplete format");
        }

        return characterQueue.peek();
    }

    /**
     * Some character only have control function so once it was parsed at the
     * right place it can be disposed. This serves a simple wrapper for that
     * functionality.
     *
     * @param  character The character for which is checked if it is head of queue.
     * @return           True if it was the head and has been removed false otherwise.
     */
    private java.lang.Boolean isHeadOfQueueAndIfSoRemove(java.lang.Character character) {
        if (characterQueue.isEmpty()) {
            throw new org.python.exceptions.ValueError("incomplete format");
        }

        if (characterQueue.peek() == character) {
            characterQueue.remove();
            return true;
        }

        return false;
    }

    /**
     * Wrapper to get the next character. It is checked that the queue is not
     * empty.
     * @return the head of {@link org.python.types.StringFormatter#characterQueue the queue}.
     */
    private java.lang.Character pollNextCharacter() {
        characterQueue.peek();
        return characterQueue.poll();
    }

    /**
     * A short wrapper for {@link java.util.LinkedList#peek() peek()} on
     * {@link StringFormatter#args the argument list} method so an error can be
     * thrown, if the list is empty.
     *
     * @return The first value of {@link StringFormatter#args the argument list}
     * @throws org.python.exceptions.TypeError If the list is empty there have
     *                                         not been enough values.
     */
    private org.python.Object peekNextArg() throws org.python.exceptions.TypeError {
        if (currentArgumentIndex < args.size()) {
            return args.get(currentArgumentIndex);
        }

        throw new org.python.exceptions.TypeError("not enough arguments for format string");
    }

    /**
     * A short wrapper for {@link java.util.LinkedList#peek() pop()} on
     * {@link StringFormatter#args the argument list} method so an error can be
     * thrown, if the list is empty.
     *
     * @return The first argument of {@link StringFormatter#args the argument list}
     * @throws org.python.exceptions.TypeError If the list is empty there have
     *                                         not been enough values.
     */
    private org.python.Object popNextArg() throws org.python.exceptions.TypeError {
        peekNextArg();
        return args.get(currentArgumentIndex++);
    }

    /**
     * If there are arguments left that have not been processed throw an Exception
     * otherwise do nothing;
     * @throws org.python.exceptions.TypeError if not all arguments were converted during string formatting
     */
    private void ensureNoArgumentsAreLeft() throws org.python.exceptions.TypeError {
        if (!singleValueIsAllowed && kwargs == null && currentArgumentIndex < args.size()) {
            throw new org.python.exceptions.TypeError(
                "not all arguments converted during string formatting");
        }
    }

    /**
     * Some special cases were no dict is passed to the formatting string
     * but keys are used in the strings. Has no side effects just throws exceptions.
     */
    private void handleMappingExceptions() {

        if (singleValueIsAllowed) {
            org.python.Object arg = peekNextArg();
            if (arg instanceof org.python.types.Range) {
                throw new org.python.exceptions.TypeError("range indices must be integers or slices, not str");
            } else if (arg instanceof org.python.types.Bytes) {
                throw new org.python.exceptions.TypeError("byte indices must be integers, not str");
            } else if (arg instanceof org.python.types.ByteArray) {
                throw new org.python.exceptions.TypeError(arg.typeName() + " indices must be integers");
            } else {
                throw new org.python.exceptions.TypeError(arg.typeName() + " indices must be integers, not str");
            }
        }

        if (kwargs == null) {
            throw new org.python.exceptions.TypeError("format requires a mapping");
        }
    }

    /*############################## =- Class -= #############################*/
    /*--- Private class methods  ---------------------------------------------*/
    /**
     * Given a Python object and a conversion char we can determine if the
     * conversion can be applied. If not a TypeError is thrown. The errors
     * message is identical to the punctuation with Python's error message.
     * Python is very forgiving if the conversion is not intuitive (e.g. %d with
     * a float value) and in that case casts the type to whatever makes sense.
     *
     * In the case of a character conversion an OverflowError error can occur.
     * The current limit is 0x110000 since that is what Java offers.
     *
     * @param pythonObject A Python object that should be inserted into the
     *                     String after conversion.
     * @param conversion   The type of conversion that should be performed.
     *
     * @throws org.python.exceptions.TypeError     If conversion and Object does
     *                                             not match and there is not
     *                                             type case possible.
     * @throws org.python.exceptions.OverflowError In case the objects integer
     *                                             value is not in Unicode range
     */
    private static void pythonObjectMatchesConversionOrDie(org.python.Object pythonObject, char conversion)
        throws org.python.exceptions.TypeError, org.python.exceptions.OverflowError {
        java.lang.Object javaObject = pythonObject.toJava();

        if (conversion == 'c') {
            if (pythonObject instanceof org.python.types.Int || pythonObject instanceof org.python.types.Float) {
                if ((java.lang.Long) javaObject >= 0x110000) { // Unicode max => same as in Java.
                    throw new org.python.exceptions.OverflowError("%c arg not in range(0x110000)");
                }
            } else if (pythonObject instanceof org.python.types.Str && ((java.lang.String) pythonObject.toJava()).length() == 1) {
                javaObject = ((java.lang.String) javaObject).charAt(0);
            } else {
                throw new org.python.exceptions.TypeError("%c requires int or char");
            }
        } else if ("fFgGeE".indexOf(conversion) != -1) {
            if (pythonObject instanceof org.python.types.Complex) {
                throw new org.python.exceptions.TypeError("can't convert complex to float");
            } else if (!(pythonObject instanceof org.python.types.Int
                    || pythonObject instanceof org.python.types.Float
                    || pythonObject instanceof org.python.types.Bool)) {
                throw new org.python.exceptions.TypeError("a float is required");
            }
        } else if ("diouxX".indexOf(conversion) != -1) {
            if (!(pythonObject instanceof org.python.types.Int
                    || pythonObject instanceof org.python.types.Float
                    || pythonObject instanceof org.python.types.Bool)) {
                throw new org.python.exceptions.TypeError("%" + conversion + " format: a number is required, not " + pythonObject.typeName());
            }
        }
    }


    /**
     * Given the formatter has to convert a Number (Float or Int). Various
     * options determine how the formatter handles signed numbers. All options
     * are considered here and a String that should be perpended to a Number is
     * returned.
     * @param  sign The sign as a character. '\0' is the value for no sign
     *              specified.
     *
     * @param flags The conversion flag map
     * @return      Whatever should be appended to the String.
     */
    private static String determineSignToUse(char sign,
            java.util.Map<java.lang.Character, java.lang.Boolean> flags) {

        if (sign == SIGN_POSITIV) {
            if (flags.get(' ')) {
                return " ";
            } else if (flags.get('+')) {
                return "+";
            } else {
                return "";
            }
        } else if (sign == SIGN_UNDEFINED) {
            return "";
        } else {
            return "-";
        }
    }

    /**
     * A wrapper around {@link java.lang.String#format Java's string formatter}
     * for Float values.
     * @param  num             The floating point number to be formatted.
     * @param  precision       The precision, applied as one would expect.
     * @param  conversionChar  The conversion char passed to Java.
     * @param  isAlternateForm Decide if # should be included aka always include
     *                         a dot in floating point numbers.
     * @return                 The desired String correctly formatted by Java.
     */
    private static java.lang.String convertFloatingPointTypeByJavaFormatter(
            java.lang.Double num, long precision, char conversionChar, boolean isAlternateForm) {
        return java.lang.String.format(
            "%" + (isAlternateForm ? "#" : "")
                + (precision == PRECSION_NOT_SET ? "" : "." + precision)
                + conversionChar,
                    ((java.lang.Number) num).doubleValue());
    }

    /**
     * Specifies floating point formatting into fixed decimal format by Java.
     * @param  num              The floating point number to be formatted.
     * @param  precision        The precision, applied as one would expect.
     * @param  isAlternateForm  Decide if # should be included aka always include
     *                          a dot in floating point numbers.
     *
     * @return                  The desired String correctly formatted by Java.
     */
    private static java.lang.String toFixed(java.lang.Double num, long precision, boolean isAlternateForm) {
        return convertFloatingPointTypeByJavaFormatter(num, precision, 'f', isAlternateForm);
    }

    /**
     * Wraps integer formatting by Java.
     * @param  num              The floating point number to be formatted.
     * @param  precision        The precision, applied as one would expect.
     * @param conversionChar    Either e or E.
     * @param  isAlternateForm  Decide if # should be included aka always include
     *                          a dot in floating point numbers.
     *
     * @return                  The desired String correctly formatted by Java.
     */
    private static java.lang.String toExp(java.lang.Double num, long precision, char conversionChar, boolean isAlternateForm) {
        return convertFloatingPointTypeByJavaFormatter(num, precision, conversionChar, isAlternateForm);
    }

    /**
     * A wrapper around {@link java.lang.String#format Java's string formatter}
     * for Integer values.
     *
     * @param  num            Integer or Double that should be converted with
     *                        the given arguments by {@link java.lang.String#format Java's string formatter}
     *
     * @param  precision      The precision for Python's integer conversion
     *                        determines with minimum width of the outputted
     *                        number. Missing digits are padded with zeros. This
     *                        happens regardless of minimum width, padding
     *                        character flag ('0' or ' ') and adjustment.
     *
     * @param  conversionChar Conversion char is passed to Java untouched.
     * @return                The desired String correctly formatted by Java.
     */
    private static java.lang.String toInteger(java.lang.Object num, long precision, char conversionChar) {
        return java.lang.String.format("%"
            + (precision == PRECSION_NOT_SET ? 1 : "0" + precision)
            + conversionChar,
                ((java.lang.Number) num).intValue());
    }

    /**
     * Sets and resets all conversion flags. Must be called for each format
     * specifier in a format string.
     *
     * @return A new HashMap with all flags set false.
     */
    private static java.util.Map<java.lang.Character, java.lang.Boolean> getInitialConversionFlags() {
        java.util.Map<java.lang.Character, java.lang.Boolean> flags = new java.util.HashMap<>();

        flags.put('#', false);
        flags.put('0', false);
        flags.put('+', false);
        flags.put('-', false);
        flags.put(' ', false);

        return flags;
    }

    /**
     * Builds a new String of specified length filled with a given character.
     * @param  filling A char that the string should consist of.
     * @param  length  The length of the returned string.
     * @return         A string of length length which's only character is
     *                   filling.
     */
    private static String getPaddingOfLength(char filling, long length) {
        java.lang.StringBuilder buffer = new StringBuilder();
        while (length-- > 0) {
            buffer.append(filling);
        }

        return new java.lang.String(buffer);
    }

    /*############################# =- Fields -= #############################*/
    /*--- Private object variables -------------------------------------------*/
    // set by Constructor
    private final java.lang.String formatString;
    private final java.util.List<org.python.Object> args;
    private final java.util.Map<org.python.Object, org.python.Object> kwargs;

    // have default values
    private final java.lang.StringBuilder buffer = new StringBuilder();
    private final java.util.Deque<java.lang.Character> characterQueue = new java.util.LinkedList<>();;
    private java.lang.Integer currentArgumentIndex = 0;
    private boolean singleValueIsAllowed = false;



    /*############################ =- Constants -= ###########################*/
    public static final java.lang.Long DEFAULT_MINIMUM_WIDTH = 0L;
    public static final java.lang.Long DEFAULT_PRECSION = 6L;
    public static final java.lang.Long PRECSION_NOT_SET = -1L;

    public static final char SIGN_POSITIV = '-';
    public static final char SIGN_NEGATIV = '+';
    public static final char SIGN_UNDEFINED = '\0';
}
