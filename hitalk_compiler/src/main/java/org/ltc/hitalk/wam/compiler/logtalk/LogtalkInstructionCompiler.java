package org.ltc.hitalk.wam.compiler.logtalk;

import org.ltc.hitalk.compiler.IVafInterner;
import org.ltc.hitalk.core.utils.ISymbolTable;
import org.ltc.hitalk.parser.PlPrologParser;
import org.ltc.hitalk.wam.compiler.HtMethod;
import org.ltc.hitalk.wam.compiler.prolog.ICompilerObserver;
import org.ltc.hitalk.wam.compiler.prolog.PrologDefaultBuiltIn;
import org.ltc.hitalk.wam.compiler.prolog.PrologInstructionCompiler;

public class LogtalkInstructionCompiler<T extends HtMethod, P, Q> extends PrologInstructionCompiler <T, P, Q> {
    /**
     * Creates a base machine over the specified symbol table.
     *
     * @param symbolTable    The symbol table for the machine.
     * @param interner       The interner for the machine.
     * @param defaultBuiltIn
     * @param observer
     * @param parser
     */
    public LogtalkInstructionCompiler ( ISymbolTable <Integer, String, Object> symbolTable,
                                        IVafInterner interner,
                                        PrologDefaultBuiltIn defaultBuiltIn,
                                        ICompilerObserver <P, Q> observer,
                                        PlPrologParser parser ) {
        super(symbolTable, interner, defaultBuiltIn, observer, parser);
    }
}