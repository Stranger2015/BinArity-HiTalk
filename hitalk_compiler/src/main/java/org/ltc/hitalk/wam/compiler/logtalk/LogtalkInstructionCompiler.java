package org.ltc.hitalk.wam.compiler.logtalk;

import org.ltc.hitalk.compiler.IVafInterner;
import org.ltc.hitalk.core.utils.ISymbolTable;
import org.ltc.hitalk.parser.HtPrologParser;
import org.ltc.hitalk.wam.compiler.HtMethod;
import org.ltc.hitalk.wam.compiler.hitalk.HiTalkWAMCompiledPredicate;
import org.ltc.hitalk.wam.compiler.hitalk.HiTalkWAMCompiledQuery;
import org.ltc.hitalk.wam.compiler.prolog.ICompilerObserver;
import org.ltc.hitalk.wam.compiler.prolog.PrologDefaultBuiltIn;
import org.ltc.hitalk.wam.compiler.prolog.PrologInstructionCompiler;

public class LogtalkInstructionCompiler<T extends HtMethod, P, Q, PC extends HiTalkWAMCompiledPredicate, QC extends HiTalkWAMCompiledQuery> extends PrologInstructionCompiler<T, P, Q, PC, QC> {
    /**
     * Creates a base machine over the specified symbol table.
     *
     * @param symbolTable    The symbol table for the machine.
     * @param interner       The interner for the machine.
     * @param defaultBuiltIn
     * @param observer
     * @param parser
     */
    public LogtalkInstructionCompiler(ISymbolTable<Integer, String, Object> symbolTable,
                                      IVafInterner interner,
                                      PrologDefaultBuiltIn defaultBuiltIn,
                                      ICompilerObserver<P, Q> observer,
                                      HtPrologParser parser) {
        super(symbolTable, interner, defaultBuiltIn, observer, parser);
    }
}
