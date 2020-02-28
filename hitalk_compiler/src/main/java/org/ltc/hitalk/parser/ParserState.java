package org.ltc.hitalk.parser;

/**
 *
 */
public enum ParserState {
    START,
    FINISH,

    EXPR_A,
    EXPR_A_EXIT,
    EXPR_B,
    EXPR_B_EXIT,
    EXPR_C,
    EXPR_C_EXIT,
    EXPR_A0,
    EXPR_A0_EXIT,

    EXPR_A0_BRACE,
    EXPR_A0_BRACE_EXIT,

    EXPR_A0_BRACKET,
    EXPR_A0_BRACKET_EXIT,

    EXPR_A0_ARGS,
    EXPR_A0_ARGS_EXIT,

    EXPR_A0_HEADS,
    EXPR_A0_HEADS_EXIT,

    EXPR_A0_TAIL,
    EXPR_A0_TAIL_EXIT,

    SEQUENCE,
    SEQUENCE_EXIT,
    NOP;

    private final ParserState[] parserStates;

    ParserState(ParserState... parserStates) {
        this.parserStates = parserStates;
    }

    private Boolean or;

    ParserState(boolean or, ParserState... parserStates) {
        this.or = or;
        this.parserStates = parserStates;
    }

    public ParserState[] getParserStates() {
        return parserStates;
    }

    public Boolean getOr() {
        return or;
    }
}