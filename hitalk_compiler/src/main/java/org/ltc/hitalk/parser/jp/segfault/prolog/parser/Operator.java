package org.ltc.hitalk.parser.jp.segfault.prolog.parser;

import com.thesett.aima.logic.fol.Functor;
import com.thesett.aima.logic.fol.OpSymbol;
import com.thesett.aima.logic.fol.Term;
import org.jetbrains.annotations.NotNull;

import static org.ltc.hitalk.parser.jp.segfault.prolog.parser.Operator.Associativity.*;
import static org.ltc.hitalk.parser.jp.segfault.prolog.parser.Operator.Fixity.*;
import static org.ltc.hitalk.term.Atom.EMPTY_TERM_ARRAY;


/**
 * Prologの演算子を表現します。
 *
 * @author shun
 */
public class Operator extends Functor implements Comparable, Cloneable {
    public final Associativity associativity;
    public final String textName;
    public int lprio;
    public int rprio;
    private int priority;


    /**
     * @param priority
     * @param associativity
     * @param notation
     * @param lprio
     * @param rprio
     */
    public Operator ( int priority, Associativity associativity, int notation, int lprio, int rprio ) {
        super(notation, null);
        this.priority = priority;
        this.associativity = associativity;
        this.lprio = lprio;
        this.rprio = rprio;
        textName = "";
    }

//    /**
//     * @param priority
//     * @param associativity
//     * @param notation
//     * @param textName
//     */
//    public Operator ( int priority, Associativity associativity, int notation, String textName ) {
//        this(priority, associativity, notation, associativity.round() == fx ? -1 : priority * 2 + associativity.lprio(), associativity.round() == xf ? -1 : priority * 2 + associativity.rprio());
//    }

    /**
     * Creates a new functor with the specified arguments.
     *
     * @param name      The name of the functor.
     * @param arguments The functors arguments.
     * @param textName
     */
    public Operator ( int name, Term[] arguments, Associativity associativity, String textName, int priority ) {
        super(name, arguments);
        this.associativity = associativity;
        this.textName = textName;
        this.priority = priority;
    }

    public Operator ( int priority, Associativity associativity, int name, String textName ) {
        super(name, EMPTY_TERM_ARRAY);
        this.priority = priority;
        this.associativity = associativity;
        this.textName = textName;
    }

    public Operator ( int priority, String textName, Associativity associativity ) {
        this(-priority, associativity, -1, textName);
    }

    /**
     * Creates a new functor with the specified arguments.
     *
     * @param name          The name of the functor.
     * @param arguments     The functors arguments.
     * @param associativity
     * @param textName
     */
    public Operator ( int name, Term[] arguments, Associativity associativity, String textName ) {
        super(name, arguments);
        this.associativity = associativity;
        this.textName = textName;
    }

    /**
     * Provides the symbols fixity, derived from its associativity.
     *
     * @return
     */
    public Fixity getFixity () {
        switch (associativity) {
            case fx:
            case fy:
                return Pre;

            case xf:
            case yf:
                return Post;

            case xfx:
            case xfy:
            case yfx:
                return In;

            case x:
                break;
            default:
                throw new IllegalStateException("Unknown associativity.");
        }

        return null;
    }

    /**
     * 与えられた演算子の並び順が表記上正しいかどうかを判別します。
     */
    public static boolean isCorrectOrder ( Associativity l, Associativity r ) {
        l = l.round();
        r = r.round();
        switch (r) {
            case x:
            case fx:
                return l != x && l != xf;
            case xfx:
            case xf:
                return l != fx && l != xfx;
            default:
                throw new IllegalStateException(l + ", " + r);
        }
    }

    @Override
    public boolean equals ( Object o ) {
        if (o instanceof Operator) {
            Operator that = (Operator) o;
            return associativity.round() == that.associativity.round() && getName() == that.getName();
        }
        return false;
    }

    @Override
    public String toString () {
        return "op(" + lprio + ":" + rprio + ", " + associativity + ", '" + getName() + "')";
    }

    /**
     * Defines the possible operator fixities.
     */
    public enum Fixity {
        /**
         * Pre-fix.
         */
        Pre,

        /**
         * Post-fix.
         */
        Post,

        /**
         * In-fix.
         */
        In
    }


    /**
     *
     */
    public enum Associativity {

        /**
         * 中置の二項演算子です。
         */
        xfx(2),

        /**
         * 中置の二項演算子です。(右結合)
         */
        xfy(2),

        /**
         * 中置の二項演算子です。(左結合)
         */
        yfx(2),

        /**
         * 前置演算子です。
         */
        fx(1), fy(1),

        /**
         * 前置演算子です。
         */
        xf(1), yf(1),

        /**
         * オペランドを表現します。
         */
        x(0);

        /**
         * この演算子が結合する項数です。
         */
        public final int arity;

        Associativity ( int arity ) {
            this.arity = arity;
        }

        public Associativity round () {
            switch (this) {
                case xfy:
                case yfx:
                    return xfx;
                case fy:
                    return fx;
                case yf:
                    return xf;
                default:
                    return this;
            }
        }

        public int lprio () {
            switch (this) {
                case yfx:
                case yf:
                    return 1;
                default:
                    return 0;
            }
        }

        public int rprio () {
            switch (this) {
                case xfy:
                case fy:
                    return 1;
                default:
                    return 0;
            }
        }
    }


    /**
     * Reports whether this operator is an prefix operator.
     *
     * @return <tt>true <tt>if this operator is an prefix operator.
     */
    public boolean isPrefix () {
        return ((this.associativity == fx) || (associativity == fy));
    }

    /**
     * Reports whether this operator is an postfix operator.
     *
     * @return <tt>true <tt>if this operator is an postfix operator.
     */
    public boolean isPostfix () {
        return ((associativity == xf) || (associativity == yf));
    }

    /**
     * Reports whether this operator is an infix operator.
     *
     * @return <tt>true <tt>if this operator is an infix operator.
     */
    public boolean isInfix () {
        return ((associativity == xfy) || (associativity == yfx) || (associativity == xfx));
    }

    /**
     * Reports whether this operatis is right associative.
     *
     * @return <tt>true</tt> if this operatis is right associative.
     */
    public boolean isRightAssociative () {
        return ((associativity == fy) || (associativity == xfy));
    }

    /**
     * Reports whether this operatis is left associative.
     *
     * @return <tt>true</tt> if this operatis is left associative.
     */
    public boolean isLeftAssociative () {
        return ((associativity == yf) || (associativity == yfx));
    }

    /**
     * Compares this object with the specified object for order, providing a negative integer, zero, or a positive
     * integer as this symbols priority is less than, equal to, or greater than the comparator. If this symbol is 'less'
     * than another that means that it has a lower priority value, which means that it binds more tightly.
     *
     * @param o The object to be compared with.
     * @return A negative integer, zero, or a positive integer as this symbols priority is less than, equal to, or
     * greater than the comparator.
     */
    public int compareTo ( @NotNull Object o ) {
        return priority - ((Operator) o).priority;
    }

    /**
     * @param op
     * @return
     */
    public static OpSymbol convertOperatorToOpSymbol ( Operator op ) {
        return new OpSymbol(-1, "", convertAssoc(op), op.priority);
    }

    /**
     * @param op
     * @return
     */
    public static OpSymbol.Associativity convertAssoc ( Operator op ) {
        return OpSymbol.Associativity.valueOf(op.associativity.toString().toUpperCase());
    }
}