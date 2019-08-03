package org.ltc.hitalk.parser;

import com.thesett.aima.logic.fol.Clause;
import com.thesett.aima.logic.fol.Functor;
import org.ltc.hitalk.entities.HtEntityIdentifier;

/**
 *
 */
public
class HtClause extends Clause <Functor> {
    private final HtEntityIdentifier identifier;

    /**
     * @param head
     * @param body
     */
    public
    HtClause ( Functor head, Functor[] body ) {
        this(null, head, body);
    }

    /**
     * Creates a program sentence in L2.
     *
     * @param head The head of the program.
     * @param body The functors that make up the query body of the program, if any. May be <tt>null</tt>
     */
    public
    HtClause ( HtEntityIdentifier identifier, Functor head, Functor[] body ) {
        super(head, body);

        this.identifier = identifier;
    }
}