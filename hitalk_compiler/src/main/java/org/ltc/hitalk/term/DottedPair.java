package org.ltc.hitalk.term;

import com.thesett.aima.logic.fol.RecursiveList;
import com.thesett.aima.logic.fol.Term;

import java.util.Iterator;

import static org.ltc.hitalk.term.Atom.EMPTY_TERM_ARRAY;
import static org.ltc.hitalk.term.DottedPair.Kind.NIL;

/**
 *
 */
public class DottedPair extends RecursiveList implements Term {
    public DottedPair () {
        this(NIL, EMPTY_TERM_ARRAY);
    }

    public enum Kind {
        NIL, //"[]" "{}" "()" BY  INTERNED NAME

        LIST, //-1 [.......]
        BYPASS,//-2
        AND,//-3 blocked  term
    }

    /**
     * Creates a list functor with the specified interned name and arguments.
     *
     * @param arguments The functors arguments.
     */
    public DottedPair ( Kind type, Term[] arguments ) {
        super(-type.ordinal(), arguments);
    }

    @Override
    public Iterator <Term> iterator () {
        return null;
    }//todo

    @Override
    public boolean isNil () {
        return arguments.length == 0;
    }
}
