package org.ltc.hitalk.compiler;

import com.thesett.aima.logic.fol.LogicCompilerObserver;
import com.thesett.aima.logic.fol.Sentence;
import com.thesett.common.parsing.SourceCodeException;
import com.thesett.common.util.doublemaps.SymbolKey;
import org.ltc.hitalk.core.ICompiler;
import org.ltc.hitalk.core.IResolver;
import org.ltc.hitalk.core.utils.ISymbolTable;
import org.ltc.hitalk.entities.HtProperty;
import org.ltc.hitalk.interpreter.DcgRule;
import org.ltc.hitalk.parser.HtClause;
import org.ltc.hitalk.parser.PlPrologParser;
import org.ltc.hitalk.parser.PlTokenSource;
import org.ltc.hitalk.term.ITerm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import static com.thesett.aima.logic.fol.wam.compiler.SymbolTableKeys.SYMKEY_PREDICATES;

/**
 *
 *
 * @param <P>
 * @param <Q>
 */
abstract
public class BaseCompiler<T extends HtClause, P, Q> extends AbstractBaseMachine
        implements ICompiler <T, P, Q> {

    protected final Logger logger = LoggerFactory.getLogger(getClass().getSimpleName());

    protected ISymbolTable <Integer, String, Object> scopeTable;
    protected int scope;
    protected Deque <SymbolKey> predicatesInScope = new ArrayDeque <>();
    protected PlPrologParser parser;
    protected IResolver <T, Q> resolver;
    protected LogicCompilerObserver <P, Q> observer;//todo is that really to be here

    /**
     * @param symbolTable
     * @param interner
     * @param parser
     */
    protected BaseCompiler ( ISymbolTable <Integer, String, Object> symbolTable,
                             IVafInterner interner,
                             PlPrologParser parser,
                             LogicCompilerObserver <P, Q> observer ) {

        super(symbolTable, interner);
        this.parser = parser;
        this.observer = observer;
    }

    public BaseCompiler () {
        super();
    }

    //    @Override
    public void compile ( Sentence <T> sentence ) throws SourceCodeException {
        logger.debug("compile(PlSentence<T> sentence = " + sentence + "): called... ");

        // Extract the clause to compile from the parsed sentence.
        HtClause clause = sentence.getT();

        // Classify the sentence to compile by the different sentence types in the language.
        if (clause.isQuery()) {
            compileQuery((Q) clause);//FIXME
        } else {
            // Initialize a nested symbol table for the current compilation scope, if it has not already been.
            if (scopeTable == null) {
                scopeTable = symbolTable.enterScope(scope);
            }

            // Check in the symbol table, if a compiled predicate with name matching the program clause exists, and if
            // not create it.
            SymbolKey predicateKey = scopeTable.getSymbolKey(clause.getHead().getName());
            List <HtClause> clauses = (List <HtClause>) scopeTable.get(predicateKey, SYMKEY_PREDICATES);

            if (clauses == null) {
                clauses = new ArrayList <>();
                scopeTable.put(predicateKey, SYMKEY_PREDICATES, clauses);
                predicatesInScope.offer(predicateKey);
            }

            // Add the clause to compile to its parent predicate for compilation at the end of the current scope.
            clauses.add(clause);
        }
    }

    /**
     * Establishes an observer on the compiled forms that the compiler outputs.
     *
     * @param observer The compiler output observer.
     */
    public void setCompilerObserver ( LogicCompilerObserver <P, Q> observer ) {
        this.observer = observer;
    }

    public IResolver <T, Q> getResolver () {
        return resolver;
    }

    @Override
    public Logger getConsole () {
        return logger;
    }

    @Override
    public PlPrologParser getParser () {
        return parser;
    }

    @Override
    public void compileDcgRule ( DcgRule rule ) throws SourceCodeException {

    }

    @Override
    public void compile ( String fileName, HtProperty... flags ) throws IOException, SourceCodeException {
        PlTokenSource ts = PlTokenSource.getTokenSourceForIoFile(new File(fileName));
        compile(ts, flags);
    }

    @Override
    public void compile ( PlTokenSource tokenSource, HtProperty... flags ) throws IOException, SourceCodeException {
        getConsole().info("Compiling " + tokenSource.getPath() + "... ");
        parser.setTokenSource(tokenSource);
        while (true) {
            ITerm t = parser.next();
            if (t == null) {
                break;
            }
            T c = (T) parser.convert(t);//FIXME
            compile(c, flags);
        }
    }

    public LogicCompilerObserver <P, Q> getObserver () {
        return observer;
    }
}
