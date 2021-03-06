    /*
     * Copyright The Sett Ltd, 2005 to 2014.
     *
     * Licensed under the Apache License, Version 2.0 (the "License");
     * you may not use this file except in compliance with the License.
     * You may obtain a copy of the License at
     *
     *     http://www.apache.org/licenses/LICENSE-2.0
     *
     * Unless required by applicable law or agreed to in writing, software
     * distributed under the License is distributed on an "AS IS" BASIS,
     * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
     * See the License for the specific language governing permissions and
     * limitations under the License.
     */
    package org.ltc.hitalk.wam.compiler.builtins;

    import com.thesett.common.util.SizeableLinkedList;
    import org.ltc.hitalk.term.ITerm;
    import org.ltc.hitalk.wam.compiler.HtFunctorName;
    import org.ltc.hitalk.wam.compiler.HtWAMLabel;
    import org.ltc.hitalk.wam.compiler.IFunctor;
    import org.ltc.hitalk.wam.compiler.hitalk.HiTalkDefaultBuiltIn;
    import org.ltc.hitalk.wam.compiler.hitalk.HiTalkWAMInstruction;
    import org.ltc.hitalk.wam.compiler.prolog.IPrologBuiltIn;

    import java.util.ArrayList;
    import java.util.List;

    import static com.thesett.aima.logic.fol.wam.compiler.SymbolTableKeys.SYMKEY_PERM_VARS_REMAINING;
    import static org.ltc.hitalk.wam.compiler.hitalk.HiTalkWAMInstruction.HiTalkWAMInstructionSet.*;

    /**
     * Disjunction implements the Prolog disjunction operator ';' that sets up multiple choice points potentially leading to
     * multiple solutions.
     *
     * <pre><p/><table id="crc"><caption>CRC Card</caption>
     * <tr><th> Responsibilities <th> Collaborations
     * <tr><td> Implement the disjunction operator.
     * </table></pre>
     *
     * @author Rupert Smith
     */
    public class HtDisjunction extends HiTalkBaseBuiltIn {

        public HtDisjunction(IFunctor functor, HiTalkDefaultBuiltIn defaultBuiltIn) throws Exception {
            super(functor, defaultBuiltIn);
        }

        /**
         * {@inheritDoc}
         */
        public SizeableLinkedList<HiTalkWAMInstruction> compileBodyArguments(IFunctor functor,
                                                                             boolean isFirstBody,
                                                                             HtFunctorName clauseName,
                                                                             int bodyNumber) throws Exception {
            SizeableLinkedList<HiTalkWAMInstruction> result = new SizeableLinkedList<>();
            SizeableLinkedList<HiTalkWAMInstruction> instructions;

            // Invent some unique names for choice points within a clause.
            clauseName = new HtFunctorName(clauseName.getName() + "_" + bodyNumber, 0);

            HtFunctorName choicePointRootName = new HtFunctorName(clauseName.getName() + "_ilc", 0);
            HtFunctorName continuationPointName = new HtFunctorName(clauseName.getName() + "_cnt", 0);

            // Labels the continuation point to jump to, when a choice point succeeds.
            HtWAMLabel continueLabel = new HtWAMLabel(continuationPointName, 0);

            // Do a loop over the children of this disjunction, and any child disjunctions encountered. This could be a
            // search? or just recursive exploration. I think it will need to be a DFS.
            List<ITerm> expressions = new ArrayList<>();
            gatherDisjunctions((HtDisjunction) functor, expressions);

            for (int i = 0; i < expressions.size(); i++) {
                IFunctor expression = (IFunctor) expressions.get(i);

                boolean isFirst = i == 0;
                boolean isLast = i == (expressions.size() - 1);

                // Labels the entry point to each choice point.
                HtWAMLabel entryLabel = new HtWAMLabel(choicePointRootName, i);

                // Label for the entry point to the next choice point, to backtrack to.
                HtWAMLabel retryLabel = new HtWAMLabel(choicePointRootName, i + 1);

                if (isFirst && !isLast) {
                    // try me else.
                    result.add(new HiTalkWAMInstruction(entryLabel, TryMeElse, retryLabel));
                } else if (!isFirst && !isLast) {
                    // retry me else.
                    result.add(new HiTalkWAMInstruction(entryLabel, RetryMeElse, retryLabel));
                } else if (isLast) {
                    // trust me.
                    result.add(new HiTalkWAMInstruction(entryLabel, TrustMe));
                }

                Integer permVarsRemaining =
                        (Integer) defaultBuiltIn.getSymbolTable().get(expression.getSymbolKey(),
                                SYMKEY_PERM_VARS_REMAINING);

                // Select a non-default built-in implementation to compile the functor with, if it is a built-in.
                IPrologBuiltIn builtIn;

                if (expression instanceof IPrologBuiltIn) {
                    builtIn = (IPrologBuiltIn) expression;
                } else {
                    builtIn = defaultBuiltIn;
                }

                // The 'isFirstBody' parameter is only set to true, when this is the first functor of a rule.
                instructions = builtIn.compileBodyArguments(expression, false, clauseName, i);
                result.addAll(instructions);

                // Call the body. The number of permanent variables remaining is specified for BaseApp trimming.
                instructions = builtIn.compileBodyCall(expression, false, false, false, 0 /*permVarsRemaining*/);
                result.addAll(instructions);

                // Proceed if this disjunctive branch completes successfully. This does not need to be done for the last
                // branch, as the continuation point will come immediately after.
                if (!isLast) {
                    result.add(new HiTalkWAMInstruction(null, Continue, continueLabel));
                }
            }

            result.add(new HiTalkWAMInstruction(continueLabel, NoOp));

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
            return "Disjunction: [ arguments = " + toStringArguments() + " ]";
        }

        /**
         * Gathers the functors to compile as a sequence of choice points. These exist as the arguments to disjunctions
         * recursively below the supplied disjunction. They are flattened into a list, by performing a left-to-right depth
         * first traversal over the disjunctions, and adding their arguments into a list.
         *
         * @param disjunction The disjunction to explore the arguments of.
         * @param expressions The flattened list of disjunctive terms.
         */
        private void gatherDisjunctions ( HtDisjunction disjunction, List <ITerm> expressions ) {
            // Left argument.
            gatherDisjunctionsExploreArgument(disjunction.getArgs().getHead(0), expressions);

            // Right argument.
            gatherDisjunctionsExploreArgument(disjunction.getArgs().getHead(1), expressions);
        }

        /**
         * Explores one argument of a disjunction as part of the  function.
         *
         * @param term        The argument to explore.
         * @param expressions The flattened list of disjunctive terms.
         */
        private void gatherDisjunctionsExploreArgument ( ITerm term, List <ITerm> expressions ) {
            if (term instanceof HtDisjunction) {
                gatherDisjunctions((HtDisjunction) term, expressions);
            } else {
                expressions.add(term);
            }
        }
    }
