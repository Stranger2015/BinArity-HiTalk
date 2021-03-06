package org.ltc.hitalk.wam.compiler.hilog;

import org.ltc.hitalk.compiler.IVafInterner;
import org.ltc.hitalk.core.utils.ISymbolTable;
import org.ltc.hitalk.parser.HtClause;
import org.ltc.hitalk.parser.HtPrologParser;
import org.ltc.hitalk.wam.compiler.hitalk.HiTalkWAMCompiledPredicate;
import org.ltc.hitalk.wam.compiler.hitalk.HiTalkWAMCompiledQuery;
import org.ltc.hitalk.wam.compiler.prolog.ICompilerObserver;
import org.ltc.hitalk.wam.compiler.prolog.PrologWAMCompiler;

/**
 * @param <T>
 * @param <P>
 * @param <Q>
 * @param <PC>
 * @param <QC>
 */
public class HiLogWAMCompiler<T extends HtClause, P, Q, PC extends HiTalkWAMCompiledPredicate, QC extends HiTalkWAMCompiledQuery>
        extends PrologWAMCompiler<T, P, Q, PC, QC> {
    /**
     * Creates a base machine over the specified symbol table.
     *
     * @param symbolTable The symbol table for the machine.
     * @param interner    The interner for the machine.
     * @param parser
     * @param observer
     */
    public HiLogWAMCompiler(ISymbolTable<Integer, String, Object> symbolTable,
                            IVafInterner interner,
                            HtPrologParser parser,
                            ICompilerObserver<P, Q> observer) {
        super(symbolTable, interner, parser, observer);
    }

    public HiLogWAMCompiler () {

    }
}
