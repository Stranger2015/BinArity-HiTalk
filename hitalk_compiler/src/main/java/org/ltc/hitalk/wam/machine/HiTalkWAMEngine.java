package org.ltc.hitalk.wam.machine;

import com.thesett.aima.logic.fol.Sentence;
import com.thesett.aima.logic.fol.VariableAndFunctorInterner;
import com.thesett.common.parsing.SourceCodeException;
import org.ltc.hitalk.compiler.bktables.error.ExecutionError;
import org.ltc.hitalk.core.ICompiler;
import org.ltc.hitalk.entities.HtProperty;
import org.ltc.hitalk.interpreter.DcgRule;
import org.ltc.hitalk.interpreter.HtResolutionEngine;
import org.ltc.hitalk.parser.HiTalkParser;
import org.ltc.hitalk.parser.HtClause;
import org.ltc.hitalk.parser.HtPrologParser;
import org.ltc.hitalk.wam.compiler.HtTokenSource;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.logging.Logger;

import static org.ltc.hitalk.compiler.bktables.error.ExecutionError.Kind.PERMISSION_ERROR;
import static org.ltc.hitalk.wam.compiler.HtTokenSource.getTokenSourceForInputStream;

/**
 *
 */
public
class HiTalkWAMEngine<T extends HtClause, P, Q> extends HtResolutionEngine <T, P, Q> {
    protected final Logger log = Logger.getLogger(getClass().getSimpleName());
    /**
     * HiTalkWAMEngine implements a {@link HtResolutionEngine} for an WAM-based Prolog with built-ins. This engine loads its
     * standard library of built-ins from a resource on the classpath.
     *
     * <pre><p/><table id="crc"><caption>CRC Card</caption>
     * <tr><th> Responsibilities <th> Collaborations
     * <tr><td> Load the Prolog built-in library from a resource on the classpath when the engine is reset.
     * </table></pre>
     *
     * @author Rupert Smith
     */

//Holds the name of the resource on the classpath that contains the built-in library.
    private static final String BUILT_IN_LIB = "wam_builtins.pl";
    private static final String BUILT_IN_LIB_CORE = "core.lgt";
    //    private static final String BUILT_IN_LIB = "wam_builtins.hlgt";
//    private static final String BUILT_IN_LIB = "wam_builtins.hlgt";

    /**
     * Creates a prolog parser using the specified interner.
     *
     * @param parser
     * @param interner The functor and variable name interner.
     * @param compiler
     */
    public
    HiTalkWAMEngine ( HtPrologParser parser,
                      VariableAndFunctorInterner interner, ICompiler <P, Q> compiler ) {
        super(parser, interner, compiler);
    }

    public
    HiTalkWAMEngine () {
        super();
    }

    public void setCompiler ( ICompiler <P, Q> compiler ) {
        this.compiler = compiler;
    }

    protected ICompiler <P, Q> compiler;

    /**
     * {InheritDoc}
     * <p>
     * Loads the built-in library resource specified by {@link #BUILT_IN_LIB}.
     */
    public
    void reset () {
        // Reset the resolver to completely clear out its domain.
        cleanupDomain();

        // Create a token source to load the model rules from.
        InputStream input = getClass().getClassLoader().getResourceAsStream(BUILT_IN_LIB);
        HtTokenSource tokenSource = null;
        try {
            tokenSource = getTokenSourceForInputStream(Objects.requireNonNull(input), "");
        } catch (IOException e) {
            e.printStackTrace();
            throw new ExecutionError(PERMISSION_ERROR, null);
        }

        // Set up a parser on the token source.
        HtPrologParser libParser = new HiTalkParser(tokenSource, interner);
        libParser.setTokenSource(tokenSource);

        // Load the built-ins into the domain.
        try {
            while (true) {
                Sentence <HtClause> sentence = libParser.parse();

                if (sentence == null) {
                    break;
                }

                compiler.compile(sentence.getT());
            }

            compiler.endScope();
        } catch (SourceCodeException e) {
            // There should not be any errors in the built in library, if there are then the prolog engine just
            // isn't going to work, so report this as a bug.
            throw new IllegalStateException("Got an exception whilst loading the built-in library.", e);
        }
    }

    protected
    void cleanupDomain () {

    }

    /**
     * @param sentence
     * @param flags
     * @throws SourceCodeException
     */
    @Override
    public
    void compile ( HtClause sentence, HtProperty... flags ) throws SourceCodeException {

    }

    /**
     * @param rule
     */
    @Override
    public
    void compileDcgRule ( DcgRule rule ) throws SourceCodeException {

    }

    /**
     * @param query
     */
    @Override
    public
    void compileQuery ( HtClause query ) {

    }

    /**
     * @param clause
     */
    @Override
    public
    void compileClause ( HtClause clause ) {

    }
}

