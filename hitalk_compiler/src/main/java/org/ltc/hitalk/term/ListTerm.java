package org.ltc.hitalk.term;

import com.thesett.aima.search.Operator;
import com.thesett.aima.search.Traversable;
import org.ltc.hitalk.compiler.IVafInterner;
import org.ltc.hitalk.wam.compiler.HtFunctor;
import org.ltc.hitalk.wam.compiler.IFunctor;

import java.util.Iterator;

import static org.ltc.hitalk.term.Atom.EMPTY_TERM_ARRAY;

/**
 *
 */
public class ListTerm extends HtFunctor implements ITerm, IFunctor {

    public static final ITerm TRUE = new ListTerm(Kind.TRUE);

    /**
     *
     */
    public ListTerm () {
        super(-1, EMPTY_TERM_ARRAY);
    }

    /**
     * @param kind
     * @param arguments
     */
    public ListTerm ( Kind kind, ITerm... arguments ) {
        super(-kind.ordinal(), arguments);
    }

    /**
     * @return
     */
    public Kind getKind () {
        return Kind.values()[-getName()];
    }

    /**
     * args = name + heads + tail
     *
     * @return
     */
    public ITerm newTail () {
        ITerm[] args = getArguments();
        return args[args.length - 1];
    }

    /**
     * args = name + heads + tail
     *
     * @return
     */
    public ITerm[] getHeads () {
        ITerm[] args = getArguments();
        if (args.length <= 1) {
            return EMPTY_TERM_ARRAY;
        } else {
            int headsLen = args.length - 2;
            ITerm[] heads = new ITerm[headsLen];
            System.arraycopy(args, 1, heads, 0, headsLen);

            return heads;
        }
    }

    /**
     * @return
     */
    public Iterator <ITerm> iterator () {
        return new Iterator <ITerm>() {
            public boolean hasNext () {
                return false;
            }

            public ITerm next () {
                return null;
            }
        };
    }

    public boolean isNil () {
        return name < 0;
    }

    public int size () {
        return getHeads().length;
    }

    public ITerm get ( int i ) {
        if (size() == i) {
            return TRUE;
        }
        return getHeads()[i];
    }

    public int getArity () {
        return 0;
    }

    public boolean isDefined () {
        return false;
    }

    public String toStringArguments () {
        return null;
    }

    public void setArguments ( ITerm[] terms ) {

    }

    public int getArityInt () {
        return 0;
    }

    public String toString ( IVafInterner interner, boolean printVarName, boolean printBindings ) {
        return null;
    }


    public Traversable <ITerm> getChildStateForOperator ( Operator <ITerm> op ) {
        return null;
    }

    /**
     *
     */
    public enum Kind {
        NIL, //"[]" "{}" "()" BY  INTERNED NAME

        LIST, //-1 [.......]
        BYPASS,//-2
        AND,//-3 blocked  term
        OR,
        NOT,
        IF,
        TRUE,
        GOAL(),
        HILOG_APPLY,
        INLINE_GOAL,
        OTHER();

        private IFunctor goal;

        /**
         * @param goal
         */
        Kind ( IFunctor goal ) {
            this.goal = goal;
        }

        /**
         *
         */
        Kind () {
        }
    }
}
