package org.ltc.hitalk.wam.machine;

import com.thesett.aima.logic.fol.Resolver;
import com.thesett.common.parsing.SourceCodeException;
import org.ltc.hitalk.compiler.IVafInterner;
import org.ltc.hitalk.core.ICompiler;
import org.ltc.hitalk.interpreter.HtResolutionEngine;
import org.ltc.hitalk.parser.HiTalkParser;
import org.ltc.hitalk.parser.HtClause;
import org.ltc.hitalk.parser.IParser;
import org.ltc.hitalk.parser.jp.segfault.prolog.parser.PlPrologParser;
import org.ltc.hitalk.parser.jp.segfault.prolog.parser.PlTokenSource;
import org.ltc.hitalk.term.ITerm;

import java.io.InputStream;
import java.util.logging.Logger;

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

    /**
     * Creates a prolog parser using the specified interner.
     *
     * @param parser
     * @param interner The functor and variable name interner.
     * @param compiler
     */
    public HiTalkWAMEngine ( PlPrologParser parser,
                             IVafInterner interner,
                             ICompiler <T, P, Q> compiler,
                             Resolver <P, Q> resolver ) {
        super(parser, interner, compiler, resolver);
    }

    public void setCompiler ( ICompiler <T, P, Q> compiler ) {
        this.compiler = compiler;
    }

    protected ICompiler <T, P, Q> compiler;

    /**
     * {InheritDoc}
     * <p>
     * Loads the built-in library resource specified by {@link #BUILT_IN_LIB}.
     */
    public void reset () {
        // Reset the resolver to completely clear out its domain.
        cleanupDomain();

        // Create a token source to load the model rules from.
        InputStream input = getClass().getClassLoader().getResourceAsStream(BUILT_IN_LIB);
        PlTokenSource tokenSource = null;
        //            tokenSource = PlTokenSource.getTokenSourceForInputStream(Objects.requireNonNull(input), "");

        // Set up a parser on the token source.
        IParser libParser = new HiTalkParser(getParser());
        libParser.setTokenSource(tokenSource);

        // Load the built-ins into the domain.
        try {
            while (true) {
                HtClause sentence = libParser.sentence();

                if (sentence == null) {
                    break;
                }

                compiler.compile((T) sentence);
            }

            compiler.endScope();
        } catch (SourceCodeException e) {
            // There should not be any errors in the built in library, if there are then the prolog engine just
            // isn't going to work, so report this as a bug.
            throw new IllegalStateException("Got an exception whilst loading the built-in library.", e);
        }
    }

    protected void cleanupDomain () {

    }

    public HtClause convert ( ITerm t ) {
        return null;
    }
}