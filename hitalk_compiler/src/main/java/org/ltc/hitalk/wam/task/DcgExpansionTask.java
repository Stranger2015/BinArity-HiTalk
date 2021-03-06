package org.ltc.hitalk.wam.task;

import org.ltc.hitalk.core.IPreCompiler;
import org.ltc.hitalk.parser.Directive.DirectiveKind;
import org.ltc.hitalk.parser.HtClause;
import org.ltc.hitalk.parser.PlLexer;
import org.ltc.hitalk.term.ITerm;
import org.ltc.hitalk.term.ListTerm;
import org.ltc.hitalk.wam.compiler.IFunctor;

import java.util.EnumSet;
import java.util.List;

import static java.util.Arrays.asList;
import static org.ltc.hitalk.core.PrologBuiltIns.DCG_EXPAND;

/**
 *
 */
public class DcgExpansionTask<T extends HtClause> extends PreCompilerTask<T> {

    /**
     * @param preCompiler
     * @param tokenSource
     * @param kind
     */
    public DcgExpansionTask(
            IPreCompiler<T, PreCompilerTask<T>, ?, ?, ?, ?> preCompiler,
            PlLexer tokenSource,
            EnumSet<DirectiveKind> kind) {
        super(preCompiler, tokenSource, kind);
    }

    /**
     * @param sb
     */
    public void toString0(StringBuilder sb) {

    }

    /**
     * @param term
     * @return
     */
    @Override
    protected List<ITerm> invoke0(ITerm term) throws Exception {
        final List<ITerm> l = super.invoke0(term);
        for (ITerm t : l) {
            output.addAll(dcgExpand((IFunctor) t));
        }
        return output;
    }


    /**
     * @param f
     * @return
     */
    protected List<ListTerm> dcgExpand(IFunctor f) {
        final ListTerm lt = new ListTerm(asList(f.getArgument(0), f.getArgument(1)));
        return DCG_EXPAND.getBuiltInDef().apply(lt);
    }
}