package org.ltc.hitalk.compiler;

import com.thesett.aima.logic.fol.VariableAndFunctorInterner;
import com.thesett.common.parsing.SourceCodeException;
import org.ltc.hitalk.compiler.bktables.HiTalkFlag;
import org.ltc.hitalk.interpreter.DcgRule;
import org.ltc.hitalk.interpreter.HtResolutionEngine;
import org.ltc.hitalk.interpreter.ICompiler;
import org.ltc.hitalk.parser.HtClause;
import org.ltc.hitalk.parser.HtPrologParser;

import java.util.logging.Logger;

/**
 *
 */
public
class HiTalkEngine extends HtResolutionEngine <HtClause, HtClause> {

    /**
     * Builds an logical resolution engine from a parser, interner, compiler and resolver.
     * @param parser   The parser.
     * @param interner The interner.
     * @param compiler
     */
    public
    HiTalkEngine ( HtPrologParser parser,
                   VariableAndFunctorInterner interner,
                   ICompiler <HtClause, HtClause, HtClause> compiler ) {
        super(parser, interner, compiler);
        compiler.setResolver(this);
    }

    /**
     * Resets the engine to its default state. This will typically load any bootstrapping libraries of built-ins that
     * the engine requires, but otherwise set its domain to empty.
     */
    @Override
    public
    void reset () {
//todo
    }

    /**
     * @return
     */
    @Override
    public
    Logger getConsole () {
        return null;
    }

    /**
     * @param sentence
     * @param flags
     * @throws SourceCodeException
     */
    @Override
    public
    void compile ( HtClause sentence, HiTalkFlag... flags ) throws SourceCodeException {
//todo
    }

    /**
     * @param rule
     */
    @Override
    public
    void compileDcgRule ( DcgRule rule ) throws SourceCodeException {
//todo
    }

    /**
     * @param query
     */
    @Override
    public
    void compileQuery ( HtClause query ) {
//todo
    }

    /**
     * @param clause
     */
    @Override
    public
    void compileClause ( HtClause clause ) {
//todo
    }
}