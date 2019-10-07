package org.ltc.hitalk.wam.compiler;


import com.thesett.aima.logic.fol.*;
import com.thesett.aima.logic.fol.bytecode.BaseMachine;
import com.thesett.aima.logic.fol.wam.compiler.SymbolTableKeys;
import com.thesett.aima.search.QueueBasedSearchMethod;
import com.thesett.aima.search.SearchMethod;
import com.thesett.aima.search.util.uninformed.BreadthFirstSearch;
import com.thesett.aima.search.util.uninformed.PostFixSearch;
import com.thesett.common.util.SizeableLinkedList;
import com.thesett.common.util.doublemaps.SymbolTable;

import java.util.Collection;
import java.util.Iterator;
import java.util.TreeSet;

import static com.thesett.aima.search.util.Searches.allSolutions;
import static org.ltc.hitalk.wam.compiler.HiTalkWAMInstruction.HiTalkWAMInstructionSet.*;
import static org.ltc.hitalk.wam.compiler.HiTalkWAMInstruction.REG_ADDR;
import static org.ltc.hitalk.wam.compiler.HiTalkWAMInstruction.STACK_ADDR;

/**
 * DefaultBuiltIn implements the standard WAM Prolog compilation for normal Prolog programs. Splitting this out into
 * DefaultBuiltIn which supplies the  interface, allows different compilations to be used for built in
 * predicates that behave differently to the normal compilation.
 *
 * <pre><p/><table id="crc"><caption>CRC Card</caption>
 * <tr><th> Responsibilities <th> Collaborations
 * <tr><td> Generate instructions to set up the arguments to a call to a built-in functor.</td></tr>
 * <tr><td> Generate instructions to call to a built-in functor.</td></tr>
 * </table></pre>
 *
 * @author Rupert Smith
 */
public
class HiTalkDefaultBuiltIn extends BaseMachine implements HiTalkBuiltIn {

    /* Used for debugging. */
    /* private static final Logger log = Logger.getLogger(DefaultBuiltIn.class.getName()); */

    /**
     * Enumerates the possible ways in which a variable can be introduced to a clause.
     */
    public enum VarIntroduction {
        /**
         * Introduced by a get instruction.
         */
        Get,

        /**
         * Introduced by a put instruction.
         */
        Put,

        /**
         * Introduced by a set instruction.
         */
        Set,

        /**
         * Introduced by a unify instruction.
         */
        Unify
    }

    /**
     * This is used to keep track of registers as they are seen.
     */
    protected Collection <Integer> seenRegisters = new TreeSet <>();

    /**
     * Used to keep track of the temporary register assignment across multiple functors within a clause.
     */
    protected int lastAllocatedTempReg;

    /**
     * Creates a base machine over the specified symbol table.
     *
     * @param symbolTable The symbol table for the machine.
     * @param interner    The interner for the machine.
     */
    public
    HiTalkDefaultBuiltIn ( SymbolTable <Integer, String, Object> symbolTable, VariableAndFunctorInterner interner ) {
        super(symbolTable, interner);
    }

    /**
     * {@inheritDoc}
     */
    public
    SizeableLinkedList <HiTalkWAMInstruction> compileBodyCall ( Functor expression,
                                                                boolean isFirstBody,
                                                                boolean isLastBody,
                                                                boolean chainRule,
                                                                int permVarsRemaining ) {
        // Used to build up the results in.
        SizeableLinkedList <HiTalkWAMInstruction> instructions = new SizeableLinkedList <>();

        // Generate the call or tail-call instructions, followed by the call address, which is f_n of the
        // called program.
        if (isLastBody) {
            // Deallocate the stack frame at the end of the clause, but prior to calling the last
            // body predicate.
            // This is not required for chain rules, as they do not need a stack frame.
            if (!chainRule) {
                instructions.add(new HiTalkWAMInstruction(Deallocate));
            }

            instructions.add(new HiTalkWAMInstruction(Execute, interner.getFunctorFunctorName(expression)));
        }
        else {
            instructions.add(new HiTalkWAMInstruction(Call, (byte) (permVarsRemaining & 0xff),
                    interner.getFunctorFunctorName(expression)));
        }

        return instructions;
    }

    /**
     * {@inheritDoc}
     */
    public
    SizeableLinkedList <HiTalkWAMInstruction> compileBodyArguments (
            Functor expression,
            boolean isFirstBody,
            FunctorName clauseName,
            int bodyNumber ) {
        // Used to build up the results in.
        SizeableLinkedList <HiTalkWAMInstruction> instructions = new SizeableLinkedList <>();

        // Allocate argument registers on the body, to all functors as outermost arguments.
        // Allocate temporary registers on the body, to all terms not already allocated.
        /*if (!isFirstBody)
        {
            lastAllocatedRegister = 0;
        }*/

        allocateArgumentRegisters(expression);
        allocateTemporaryRegisters(expression);

        // Loop over all of the arguments to the outermost functor.
        int numOutermostArgs = expression.getArity();

        for (int j = 0; j < numOutermostArgs; j++) {
            Term nextOutermostArg = expression.getArgument(j);
            int allocation = (Integer) symbolTable.get(nextOutermostArg.getSymbolKey(), SymbolTableKeys.SYMKEY_ALLOCATION);

            byte addrMode = (byte) ((allocation & 0xff00) >> 8);
            byte address = (byte) (allocation & 0xff);

            // On the first occurrence of a variable output a put_var.
            // On a subsequent variable occurrence output a put_val.
            if (nextOutermostArg.isVar() && !seenRegisters.contains(allocation)) {
                seenRegisters.add(allocation);

                // The variable has been moved into an argument register.
                //varNames.remove((byte) allocation);
                //varNames.put((byte) j, ((Variable) nextOutermostArg).getName());

                /*log.fine("PUT_VAR " + ((addrMode == REG_ADDR) ? "X" : "Y") + address + ", A" + j);*/

                HiTalkWAMInstruction instruction = new HiTalkWAMInstruction(PutVar, addrMode, address, (byte) (j & 0xff));
                instructions.add(instruction);

                // Record the way in which this variable was introduced into the clause.
                symbolTable.put(nextOutermostArg.getSymbolKey(), SymbolTableKeys.SYMKEY_VARIABLE_INTRO, VarIntroduction.Put);
            }
            else if (nextOutermostArg.isVar()) {
                // Check if this is the last body functor in which this variable appears, it does so only in argument
                // position, and this is the first occurrence of these conditions. In which case, an unsafe put is to
                // be used.
                if (isLastBodyTermInArgPositionOnly(nextOutermostArg, expression) && (addrMode == STACK_ADDR)) {
                    /*log.fine("PUT_UNSAFE_VAL " + ((addrMode == REG_ADDR) ? "X" : "Y") + address + ", A" +
                        j);*/

                    HiTalkWAMInstruction instruction = new HiTalkWAMInstruction(PutUnsafeVal, addrMode, address, (byte) (j & 0xff));
                    instructions.add(instruction);

                    symbolTable.put(nextOutermostArg.getSymbolKey(), SymbolTableKeys.SYMKEY_VAR_LAST_ARG_FUNCTOR, null);
                }
                else {
                    /*log.fine("PUT_VAL " + ((addrMode == REG_ADDR) ? "X" : "Y") + address + ", A" + j);*/

                    HiTalkWAMInstruction instruction = new HiTalkWAMInstruction(PutVal, addrMode, address, (byte) (j & 0xff));
                    instructions.add(instruction);
                }
            }

            // When a functor is encountered, output a put_struc.
            else if (nextOutermostArg.isFunctor()) {
                // Heap cells are to be created in an order such that no heap cell can appear before other cells that it
                // refers to. A postfix traversal of the functors in the term to compile is used to achieve this, as
                // child functors in a head will be visited first.
                // Walk over the query term in post-fix order, picking out just the functors.
                QueueBasedSearchMethod <Term, Term> postfixSearch = new PostFixSearch <>();
                postfixSearch.reset();
                postfixSearch.addStartState(nextOutermostArg);
                postfixSearch.setGoalPredicate(new FunctorTermPredicate());

                Iterator <Term> treeWalker = allSolutions(postfixSearch);

                // For each functor encountered: put_struc.
                while (treeWalker.hasNext()) {
                    Functor nextFunctor = (Functor) treeWalker.next();
                    allocation = (Integer) symbolTable.get(nextFunctor.getSymbolKey(), SymbolTableKeys.SYMKEY_ALLOCATION);
                    addrMode = (byte) ((allocation & 0xff00) >> 8);
                    address = (byte) (allocation & 0xff);

                    // Ouput a put_struc instuction, except on the outermost functor.
                    /*log.fine("PUT_STRUC " + interner.getFunctorName(nextFunctor) + "/" + nextFunctor.getArity() +
                        ((addrMode == REG_ADDR) ? ", X" : ", Y") + address);*/

                    HiTalkWAMInstruction instruction = new HiTalkWAMInstruction(PutStruc,
                            addrMode, address,
                            interner.getDeinternedFunctorName(nextFunctor.getName()), nextFunctor);
                    instructions.add(instruction);

                    // For each argument of the functor.
                    int numArgs = nextFunctor.getArity();

                    for (int i = 0; i < numArgs; i++) {
                        Term nextArg = nextFunctor.getArgument(i);
                        allocation = (Integer) symbolTable.get(nextArg.getSymbolKey(), SymbolTableKeys.SYMKEY_ALLOCATION);
                        addrMode = (byte) ((allocation & 0xff00) >> 8);
                        address = (byte) (allocation & 0xff);

                        // If it is new variable: set_var or put_var.
                        // If it is variable or functor already seen: set_val or put_val.
                        if (nextArg.isVar() && !seenRegisters.contains(allocation)) {
                            seenRegisters.add(allocation);

                            /*log.fine("SET_VAR " + ((addrMode == REG_ADDR) ? "X" : "Y") + address);*/
                            instruction = new HiTalkWAMInstruction(SetVar, addrMode, address, nextArg);

                            // Record the way in which this variable was introduced into the clause.
                            symbolTable.put(nextArg.getSymbolKey(), SymbolTableKeys.SYMKEY_VARIABLE_INTRO, VarIntroduction.Set);
                        }
                        else {
                            // Check if the variable is 'local' and use a local instruction on the first occurrence.
                            VarIntroduction introduction = (VarIntroduction) symbolTable.get(nextArg.getSymbolKey(), SymbolTableKeys.SYMKEY_VARIABLE_INTRO);

                            if (isLocalVariable(introduction, addrMode)) {
                                /*log.fine("SET_LOCAL_VAL " + ((addrMode == REG_ADDR) ? "X" : "Y") +
                                    address);*/

                                instruction = new HiTalkWAMInstruction(SetLocalVal, addrMode, address, nextArg);

                                symbolTable.put(nextArg.getSymbolKey(), SymbolTableKeys.SYMKEY_VARIABLE_INTRO, null);
                            }
                            else {
                                /*log.fine("SET_VAL " + ((addrMode == REG_ADDR) ? "X" : "Y") + address);*/
                                instruction = new HiTalkWAMInstruction(SetVal, addrMode, address, nextArg);
                            }
                        }

                        instructions.add(instruction);
                    }
                }
            }
        }

        return instructions;
    }

    /**
     * For a predicate of arity n, the first n registers are used to receive its arguments in.
     * <p>
     * <p/>Non-variable arguments appearing directly within a functor are allocated to argument registers. This means
     * that they are directly referenced from the argument registers that pass them to predicate calls, or directly
     * referenced from the argument registers that are used to read them as call arguments.
     * <p>
     * <p/>Variables appearing as functor arguments are not allocated in this way, but are kept in registers with
     * positions higher than the number of arguments. The reason for this, is that a variable can appear multiple times
     * in an expression; it may not always be the same argument. Variables are assigned to other registers, then copied
     * into the argument registers as needed.
     * <p>
     * <p/>Argument registers are allocated by argument position within the functor. This means that gaps will be left
     * in the numbering for variables to be copied in as needed.
     *
     * @param expression The clause head functor to allocate argument registers to.
     */
    public void allocateArgumentRegisters ( Functor expression ) {
        // Assign argument registers to functors appearing directly in the argument of the outermost functor.
        // Variables are never assigned directly to argument registers.
        int reg = 0;

        for (; reg < expression.getArity(); reg++) {
            Term term = expression.getArgument(reg);

            if (term instanceof Functor) {
                /*log.fine("X" + lastAllocatedTempReg + " = " + interner.getFunctorFunctorName((Functor) term));*/

                int allocation = (reg & 0xff) | (REG_ADDR << 8);
                symbolTable.put(term.getSymbolKey(), SymbolTableKeys.SYMKEY_ALLOCATION, allocation);
            }
        }
    }

    /**
     * Allocates terms within a functor expression to registers. The outermost functor itself is not assigned to a
     * register in WAM (only in l0). Functors already directly assigned to argument registers will not be re-assigned by
     * this. Variables as arguments will be assigned but not as argument registers.
     *
     * @param expression The expression to walk over.
     */
    public void allocateTemporaryRegisters ( Term expression ) {
        // Need to assign registers to the whole syntax tree, working in from the outermost functor. The outermost
        // functor itself is not assigned to a register in l3 (only in l0). Functors already directly assigned to
        // argument registers will not be re-assigned by this, variables as arguments will be assigned.
        SearchMethod <Term> outInSearch = new BreadthFirstSearch <>();
        outInSearch.reset();
        outInSearch.addStartState(expression);

        Iterator <Term> treeWalker = allSolutions(outInSearch);

        // Discard the outermost functor from the variable allocation.
        treeWalker.next();

        // For each term encountered: set X++ = term.
        while (treeWalker.hasNext()) {
            Term term = treeWalker.next();

            if (symbolTable.get(term.getSymbolKey(), SymbolTableKeys.SYMKEY_ALLOCATION) == null) {
                int allocation = (lastAllocatedTempReg++ & 0xff) | (REG_ADDR << 8);
                symbolTable.put(term.getSymbolKey(), SymbolTableKeys.SYMKEY_ALLOCATION, allocation);
            }
        }
    }

    /**
     * Determines whether a variable is local, that is, it may only exist on the stack. When variables are introduced
     * into clauses, the way in which they are introduced is recorded using the {@link VarIntroduction} enum. When a
     * variable is being written to the heap for the first time, this check may be used to see if a 'local' variant of
     * an instruction is needed, in order to globalize the variable on the heap.
     * <p>
     * <p/>The conditions for a variable being deemed local are:
     *
     * <ul>
     * <li>For a permanent variable, not initialized in this clause with set_var or unify_var instruction.</li>
     * <li>For a temporary variable, not initialized in this clause with set_var or unify_var or put_var instruction.
     * </li>
     * </ul>
     *
     * @param introduction The entityKind of instruction that introduced the variable into the clause.
     * @param addrMode     The addressing mode of the variable, permanent variables are stack allocated.
     * @return <tt>true</tt> iff the variable is unsafe.
     */
    protected
    boolean isLocalVariable ( VarIntroduction introduction, byte addrMode ) {
        if (STACK_ADDR == addrMode) {
            return (introduction == VarIntroduction.Get) || (introduction == VarIntroduction.Put);
        }
        else {
            return introduction == VarIntroduction.Get;
        }
    }

    /**
     * Checks if a variable is appearing within the last body functor in which it occurs, and only does so within
     * argument position.
     *
     * @param var  The variable to check.
     * @param body The current body functor being processed.
     * @return <tt>true</tt> iff this is the last body functor that the variable appears in and does so only in argument
     * position.
     */
    private
    boolean isLastBodyTermInArgPositionOnly ( Term var, Functor body ) {
        return body == symbolTable.get(var.getSymbolKey(), SymbolTableKeys.SYMKEY_VAR_LAST_ARG_FUNCTOR);
    }
}