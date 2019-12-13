package org.ltc.hitalk.wam.compiler.builtins;

import com.thesett.aima.logic.fol.FunctorName;
import com.thesett.common.util.SizeableLinkedList;
import org.ltc.hitalk.term.ITerm;
import org.ltc.hitalk.wam.compiler.IFunctor;
import org.ltc.hitalk.wam.compiler.hitalk.HiTalkDefaultBuiltIn;
import org.ltc.hitalk.wam.compiler.hitalk.HiTalkWAMInstruction;
import org.ltc.hitalk.wam.compiler.prolog.PrologBuiltIn;

import static com.thesett.aima.logic.fol.wam.compiler.SymbolTableKeys.SYMKEY_PERM_VARS_REMAINING;

public class HtConjunction extends HiTalkBaseBuiltIn {
    /**
     * Creates a cut built-in to implement the specified functor.
     *
     * @param functor        The functor to implement as a built-in.
     * @param defaultBuiltIn The default built in, for standard compilation and interners and symbol tables.
     */
    public HtConjunction ( IFunctor functor, HiTalkDefaultBuiltIn defaultBuiltIn ) {
        super(functor, defaultBuiltIn);
    }

    /**
     * {@inheritDoc}
     */
    public SizeableLinkedList <HiTalkWAMInstruction> compileBodyArguments ( IFunctor functor, boolean isFirstBody,
                                                                            FunctorName clauseName, int bodyNumber ) {
        SizeableLinkedList <HiTalkWAMInstruction> result = new SizeableLinkedList <>();
        SizeableLinkedList <HiTalkWAMInstruction> instructions;

        ITerm[] expressions = functor.getArguments();

        for (final ITerm iTerm : expressions) {
            IFunctor expression = (IFunctor) iTerm;

            Integer permVarsRemaining =
                    (Integer) defaultBuiltIn.getSymbolTable().get(expression.getSymbolKey(), SYMKEY_PERM_VARS_REMAINING);

            // Select a non-default built-in implementation to compile the functor with, if it is a built-in.
            PrologBuiltIn builtIn;

            if (expression instanceof PrologBuiltIn) {
                builtIn = (PrologBuiltIn) expression;
            } else {
                builtIn = defaultBuiltIn;
            }

            // The 'isFirstBody' parameter is only set to true, when this is the first functor of a rule.
            instructions = builtIn.compileBodyArguments(expression, false, clauseName, bodyNumber);
            result.addAll(instructions);

            // Call the body. The number of permanent variables remaining is specified for BaseApp trimming.
            instructions = builtIn.compileBodyCall(expression, false, false, false,
                    0 /*permVarsRemaining*/);
            result.addAll(instructions);
        }

        return result;
    }

    /**
     * {@inheritDoc}
     */
    public SizeableLinkedList <HiTalkWAMInstruction> compileBodyCall ( IFunctor expression, boolean isFirstBody,
                                                                       boolean isLastBody, boolean chainRule,
                                                                       int permVarsRemaining ) {
        return new SizeableLinkedList <>();
    }

    /**
     * Creates a string representation of this functor, mostly used for debugging purposes.
     *
     * @return A string representation of this functor.
     */
    public String toString () {
        return "Conjunction: [ arguments = " + toStringArguments() + " ]";
    }
}

