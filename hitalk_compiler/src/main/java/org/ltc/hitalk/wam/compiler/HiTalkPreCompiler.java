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
package org.ltc.hitalk.wam.compiler;

import com.thesett.aima.logic.fol.*;
import com.thesett.aima.logic.fol.bytecode.BaseMachine;
import com.thesett.aima.logic.fol.compiler.SymbolKeyTraverser;
import com.thesett.aima.logic.fol.compiler.TermWalker;
import com.thesett.aima.logic.fol.wam.TermWalkers;
import com.thesett.aima.search.util.backtracking.DepthFirstBacktrackingSearch;
import com.thesett.common.parsing.SourceCodeException;
import com.thesett.common.util.doublemaps.SymbolTable;
import org.ltc.hitalk.compiler.HiTalkBuiltInTransform;
import org.ltc.hitalk.compiler.bktables.IApplication;
import org.ltc.hitalk.core.ICompiler;
import org.ltc.hitalk.entities.HtPredicate;
import org.ltc.hitalk.parser.HtClause;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * PreCompiler transforms clauses for compilation, substituting built-ins for any built-ins in the source expressions to
 * compile.
 *
 * <pre><p/><table id="crc"><caption>CRC Card</caption>
 * <tr><th> Responsibilities <th> Collaborations
 * <tr><td> Perform the built-ins transformation.
 * </table></pre>
 *
 * @author Rupert Smith
 */
abstract public
class HiTalkPreCompiler extends BaseMachine
        implements ICompiler <HtClause, HtClause, HtClause> {

    //Used for debugging.
    protected final Logger logger = LoggerFactory.getLogger(getClass().getSimpleName());

    /**
     * Holds the default built in, for standard compilation and interners and symbol tables.
     */
    protected final HiTalkDefaultBuiltIn defaultBuiltIn;
    private final HiTalkWAMCompiler compiler;

    /**
     * Holds the built in transformation.
     */
    protected final HiTalkBuiltInTransform <IApplication <HtClause>, HtClause> builtInTransform;

    /**
     * Holds the compiler output observer.
     */
    protected LogicCompilerObserver <HtPredicate, HtClause> observer;

    /**
     * Creates a new PreCompiler.
     *
     * @param symbolTable    The symbol table.
     * @param interner       The machine to translate functor and variable names.
     * @param defaultBuiltIn The default built in, for standard compilation and interners and symbol tables.
     */
    public
    HiTalkPreCompiler ( SymbolTable <Integer, String, Object> symbolTable,
                        VariableAndFunctorInterner interner,
                        HiTalkDefaultBuiltIn defaultBuiltIn,
                        Resolver <HtClause, HtClause> resolver,
                        HiTalkWAMCompiler compiler ) {
        super(symbolTable, interner);

        this.defaultBuiltIn = defaultBuiltIn;
        this.compiler = compiler;
        builtInTransform = new HiTalkBuiltInTransform(defaultBuiltIn, compiler, resolver);//TODO GLOBAL CTX NEEDED!!

    }

    /**
     * {@inheritDoc}
     */
    public abstract
    void compile ( Sentence <HtClause> sentence ) throws SourceCodeException;


    /**
     * @param clauses
     */
    protected abstract
    void saveResult ( List <HtClause> clauses );

    /**
     * @param t
     * @return
     */
    protected abstract
    List <HtClause> preprocess ( HtClause t );

    /**
     * {@inheritDoc}
     */
    public
    void endScope () {

    }

    /**
     * Substitutes built-ins within a clause, with their built-in definitions.
     *
     * @param clause The clause to transform.
     */
    private
    void substituteBuiltIns ( Term clause ) {
        TermWalker walk = TermWalkers.positionalWalker(new HiTalkBuiltInTransformVisitor(symbolTable, interner, null, builtInTransform));
        walk.walk(clause);
    }

    /**
     * Runs a symbol key traverser over the clause to be compiled, to ensure that all of its terms and sub-terms have
     * their symbol keys initialised.
     *
     * @param clause The clause to initialise the symbol keys of.
     */
    private
    void initializeSymbolTable ( Term clause ) {
        // Run the symbol key traverser over the clause, to ensure that all terms have their symbol keys correctly
        // set up.
        SymbolKeyTraverser symbolKeyTraverser = new SymbolKeyTraverser(interner, symbolTable, null);
        symbolKeyTraverser.setContextChangeVisitor(symbolKeyTraverser);

        TermWalker symWalker = new TermWalker(new DepthFirstBacktrackingSearch <>(), symbolKeyTraverser, symbolKeyTraverser);
        symWalker.walk(clause);
    }

    /**
     * Finds and marks all functors within the clause that are considered to be top-level.
     *
     * @param clause The clause to top-level check.
     */
    private
    void topLevelCheck ( Term clause ) {
        TermWalker walk = TermWalkers.positionalWalker(new HiTalkTopLevelCheckVisitor(interner, symbolTable, null));
        walk.walk(clause);
    }

    public
    HiTalkDefaultBuiltIn getDefaultBuiltIn () {
        return defaultBuiltIn;
    }

    public
    LogicCompilerObserver <HtPredicate, HtClause> getObserver () {
        return observer;
    }

    public
    LogicCompilerObserver <HtClause, HtClause> getCompilerObserver ( HiTalkWAMCompiler.ClauseChainObserver clauseChainObserver ) {
        return null;
    }
}

