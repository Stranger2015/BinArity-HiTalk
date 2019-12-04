package org.ltc.hitalk.wam.compiler.hitalk.logtalk;

import com.thesett.aima.logic.fol.LogicCompilerObserver;
import com.thesett.common.parsing.SourceCodeException;
import com.thesett.common.util.doublemaps.SymbolTable;
import org.ltc.hitalk.compiler.BaseCompiler;
import org.ltc.hitalk.compiler.IVafInterner;
import org.ltc.hitalk.core.IResolver;
import org.ltc.hitalk.entities.HtProperty;
import org.ltc.hitalk.parser.jp.segfault.prolog.parser.PlPrologParser;
import org.ltc.hitalk.wam.compiler.HiTalkWAMCompiledQuery;
import org.ltc.hitalk.wam.compiler.HtMethod;
import org.ltc.hitalk.wam.compiler.prolog.PrologWAMCompiler;

public class LogtalkCompiler<T extends HtMethod, P, Q, PC, QC> extends BaseCompiler <T, P, Q> {

    /**
     *
     */
    protected final LogtalkTranspiler <T, P, Q> transpiler;
    protected final PrologWAMCompiler <T, P, Q, PC, QC> prologWAMCompiler;

    /**
     * @param symbolTable
     * @param interner
     * @param parser
     * @param observer
     * @param transpiler
     * @param prologWAMCompiler
     */
    protected LogtalkCompiler ( SymbolTable <Integer, String, Object> symbolTable,
                                IVafInterner interner,
                                PlPrologParser parser,
                                LogicCompilerObserver <P, Q> observer,
                                LogtalkTranspiler <T, P, Q> transpiler, PrologWAMCompiler <T, P, Q, PC, QC> prologWAMCompiler ) {
        super(symbolTable, interner, parser, observer);
        this.transpiler = transpiler;
        this.prologWAMCompiler = prologWAMCompiler;
    }

    /**
     * @param query
     */
    public void compileQuery ( HiTalkWAMCompiledQuery query ) throws SourceCodeException {

    }

    /**
     * @param resolver
     */
    public void setResolver ( IResolver <P, Q> resolver ) {

    }

    @Override
    public void endScope () throws SourceCodeException {
        transpiler.endScope();

    }

    /**
     * @param clause
     * @param flags
     * @throws SourceCodeException
     */
    public void compile ( T clause, HtProperty... flags ) throws SourceCodeException {

    }

    /**
     * @param query
     */
    public void compileQuery ( Q query ) throws SourceCodeException {

    }

    public void compile ( T clause ) {

    }
}
