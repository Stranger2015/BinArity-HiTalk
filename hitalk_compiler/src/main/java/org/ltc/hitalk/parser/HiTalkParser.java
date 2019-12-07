package org.ltc.hitalk.parser;

import com.thesett.aima.logic.fol.Sentence;
import com.thesett.common.parsing.SourceCodeException;
import org.ltc.hitalk.ITermFactory;
import org.ltc.hitalk.compiler.IVafInterner;
import org.ltc.hitalk.compiler.bktables.IOperatorTable;
import org.ltc.hitalk.parser.jp.segfault.prolog.parser.ParseException;
import org.ltc.hitalk.term.HlOpSymbol.Associativity;
import org.ltc.hitalk.term.ITerm;
import org.ltc.hitalk.term.io.HiTalkStream;
import org.ltc.hitalk.wam.compiler.Language;

import java.io.IOException;

import static org.ltc.hitalk.parser.PrologAtoms.*;
import static org.ltc.hitalk.term.HlOpSymbol.Associativity.*;
import static org.ltc.hitalk.wam.compiler.Language.HITALK;

/**
 *
 */
public class HiTalkParser extends PlPrologParser {

    public HiTalkParser () {
        super();
    }

    /**
     * @return
     */
    @Override
    public Language language () {
        return HITALK;
    }

    /**
     * public
     * prolog parser on a token source to be parsed.
     *
     * @param interner The interner for variable and functor names.
     */
    public HiTalkParser ( HiTalkStream stream,
                          IVafInterner interner,
                          ITermFactory factory,
                          IOperatorTable optable ) {
        super(stream, interner, factory, optable);
    }

    /**
     * Interns and inserts into the operator table all of the built in operators and functors in Prolog.
     */
    public void initializeBuiltIns () {
        super.initializeBuiltIns();
//Logtalk operators
        internOperator(COLON_COLON, 600, xfy);
        internOperator(COLON_COLON, 600, fy);// message sending to "self"
        internOperator(UP_UP, 600, fy);// "super" call (calls an inherited or imported method definition)
        // mode operator
        internOperator(PLUS, 200, fy);// input argument (instantiated); ISO Prolog standard operator
        internOperator(AT, 200, fy);// input argument (not modified by the call)
        internOperator(QUESTION, 200, fy);// input/output argument
        internOperator(MINUS, 200, fy);// output argument (not instantiated); ISO Prolog standard operator
        internOperator(PLUS_PLUS, 200, fy);// ground argument
        internOperator(MINUS_MINUS, 200, fy);// unbound argument (typically when returning an opaque term)
        internOperator(LSHIFT, 400, yfx);// bitwise left-shift operator (used for context-switching calls)
        // some backend Prolog compilers don't declare this ISO Prolog standard operator!
        internOperator(RSHIFT, 400, yfx);// bitwise right-shift operator (used for lambda expressions)
        // some backend Prolog compilers don't declare this ISO Prolog standard operator!
        internOperator(AS, 700, xfx);// predicate alias operator (alternative to the ::/2 or :/2 operators depending on the context)
// first introduced in SWI-Prolog and YAP also for defining aliases to module predicates

// HiTalk operator
        internOperator(PUBLIC, 1150, fx);
        internOperator(PROTECTED, 1150, fx);
        internOperator(PRIVATE, 1150, fx);
        internOperator(ENUMERATION, 1150, fx);
    }

    @Override
    public HtClause parseClause () throws ParseException, IOException, SourceCodeException {
        return super.parseClause();
    }

    @Override
    public Sentence <ITerm> parse () throws SourceCodeException, IOException, ParseException {
        return super.parse();
    }

    @Override
    public void setTokenSource ( PlTokenSource source ) {
        super.setTokenSource(source);
    }

    /**
     * @return
     */
    @Override
    public PlTokenSource getTokenSource () {
        return super.getTokenSource();
    }

    /**
     * @param name
     * @param priority
     * @param associativity
     */
    @Override
    public void internOperator ( String name, int priority, Associativity associativity ) {
        super.internOperator(name, priority, associativity);
    }
}