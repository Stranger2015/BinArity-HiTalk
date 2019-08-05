package org.ltc.hitalk.compiler.bktables.error;

/**
 *
 */
public
class ExecutionError extends RuntimeException {
    public
    Kind getKind () {
        return kind;
    }

    public
    enum Kind {
        TYPE_ERROR("Type error"),
        PERMISSION_ERROR("Permission error"),
        EXISTENCE_ERROR("Existence error"),
        DOMAIN_ERROR("Domain error"),
        INSTANTIATION_ERROR("Instantiation error"),
        RESOURCE_ERROR("Resource error"),
        REPRESENTATION_ERROR("Representation error"),
        ;

        private final String kindString;

        /**
         * @param kindString
         */
        private
        Kind ( String kindString ) {
            this.kindString = kindString;
        }

        /**
         * @return
         */
        public
        String getKindString () {
            return kindString;
        }
    }

    private final String message;
    private final Kind kind;
    private final String kindString;

    /**
     * Constructs a new runtime exception with the specified detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public
    ExecutionError ( String message, Kind kind, String kindString ) {
        this.kindString = kindString;
        this.message = String.format(message, kindString);

        this.kind = kind;
    }

    /**
     * Constructs a new runtime exception with {@code null} as its
     * detail message.  The cause is not initialized, and may subsequently be
     * initialized by a call to {@link #initCause}.
     */
    public
    ExecutionError ( Kind kind, String kindString ) {
        this("", kind, kindString);
    }
}
