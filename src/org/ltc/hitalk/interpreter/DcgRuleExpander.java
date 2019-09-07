package org.ltc.hitalk.interpreter;

import com.thesett.aima.logic.fol.Term;
import org.ltc.hitalk.parser.HtClause;
import org.ltc.hitalk.wam.task.TermRewriteTask;
import org.ltc.hitalk.wam.transformers.ITransformer;

import java.util.List;
import java.util.function.Function;

/**
 *
 */
public
class DcgRuleExpander<T extends HtClause, TC extends Term, TT extends TermRewriteTask <T, TC, TT>>
        extends TermRewriteTask <T, TC, TT> {
    /**
     * @param action
     * @param target
     * @param transformer
     */
    public
    DcgRuleExpander ( Function <T, List <T>> action, List <TC> target, ITransformer <T, TC> transformer ) {
        super(action, target, transformer);
    }
}

