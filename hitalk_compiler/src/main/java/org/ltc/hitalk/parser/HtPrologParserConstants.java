package org.ltc.hitalk.parser;

public
interface HtPrologParserConstants {
    //Generated By:JavaCC: Do not edit this line. PrologParserConstants.java
//copied and renamed
    int EOF = 0;
    int PERIOD = 9;
    int LPAREN = 10;
    int RPAREN = 11;
    int LBRACKET = 12;
    int RBRACKET = 13;

    int DQUOTE = 14;
    int QUOTE = 15;
    int CONS = 16;
    int INTEGER_LITERAL = 17;
    int DECIMAL_LITERAL = 18;
    int HEX_LITERAL = 19;
    int FLOATING_POINT_LITERAL = 20;
    int DECIMAL_FLOATING_POINT_LITERAL = 21;
    int DECIMAL_EXPONENT = 22;
    int CHARACTER_LITERAL = 23;
    int STRING_LITERAL = 24;
    int VAR = 25;
    int FUNCTOR = 26;
    int ATOM = 27;
    int NAME = 28;
    int SYMBOLIC_NAME = 29;
    int DIGIT = 30;
    int ANYCHAR = 31;
    int LOCASE = 32;
    int UPCASE = 33;
    int SYMBOL = 34;
    int INFO = 35;
    int TRACE = 36;
    int USER = 37;
    int BOF = 38;
    int LBRACE = 39;
    int RBRACE = 40;

    int DEFAULT = 0;
    int WITHIN_COMMENT = 1;

    String[] tokenImage = {
            "<EOF>",//0
            "\" \"",
            "\"\\t\"",
            "\"\\n\"",
            "\"\\r\"",
            "\"\\f\"",
            "\"/*\"",
            "\"*/\"",
            "<token of kind 8>",
            "\".\"",
            "\"(\"",
            "\")\"",
            "\"[\"",
            "\"]\"",
            "\"\\\"\"",
            "\"\\\'\"",
            "\"|\"",
            "<INTEGER_LITERAL>",
            "<DECIMAL_LITERAL>",
            "<HEX_LITERAL>",
            "<FLOATING_POINT_LITERAL>",
            "<DECIMAL_FLOATING_POINT_LITERAL>",
            "<DECIMAL_EXPONENT>",
            "<CHARACTER_LITERAL>",
            "<STRING_LITERAL>",
            "<VAR>",
            "<FUNCTOR>",
            "<ATOM>",
            "<NAME>",
            "<SYMBOLIC_NAME>",
            "<DIGIT>",
            "<ANYCHAR>",
            "<LOCASE>",
            "<HICASE>",
            "<SYMBOL>",
            "<INFO>",
            "<TRACE>",
            "<USER>",
            "<BOF>",
            "\"{\"",
            "\"}\"",
            };
}