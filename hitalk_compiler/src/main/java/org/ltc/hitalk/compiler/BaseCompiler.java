package org.ltc.hitalk.compiler;

import com.thesett.common.util.doublemaps.SymbolKey;
import org.ltc.hitalk.core.ICompiler;
import org.ltc.hitalk.core.IResolver;
import org.ltc.hitalk.core.utils.ISymbolTable;
import org.ltc.hitalk.entities.HtProperty;
import org.ltc.hitalk.interpreter.DcgRule;
import org.ltc.hitalk.parser.HtClause;
import org.ltc.hitalk.parser.HtPrologParser;
import org.ltc.hitalk.parser.PlLexer;
import org.ltc.hitalk.wam.compiler.hitalk.HiTalkWAMCompiledPredicate;
import org.ltc.hitalk.wam.compiler.hitalk.HiTalkWAMCompiledQuery;
import org.ltc.hitalk.wam.compiler.prolog.ICompilerObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * @param <P>
 * @param <Q>
 */
abstract
public class BaseCompiler<T extends HtClause, P, Q,
        PC extends HiTalkWAMCompiledPredicate,
        QC extends HiTalkWAMCompiledQuery>

        extends AbstractBaseMachine
        implements ICompiler<T, P, Q, PC, QC> {

    protected final Logger logger = LoggerFactory.getLogger(getClass().getSimpleName());

    protected ISymbolTable<Integer, String, Object> scopeTable;
    protected int scope;
    protected Deque<SymbolKey> predicatesInScope = new ArrayDeque<>();
    protected HtPrologParser parser;
    protected IResolver<PC, QC> resolver;
    protected ICompilerObserver<P, Q> observer;//todo is that really to be here

    /**
     * @param symbolTable
     * @param interner
     * @param parser
     */
    protected BaseCompiler(ISymbolTable<Integer, String, Object> symbolTable,
                           IVafInterner interner,
                           HtPrologParser parser,
                           ICompilerObserver<P, Q> observer) {

        super(symbolTable, interner);

        this.parser = parser;
        this.observer = observer;
    }

    /**
     *
     */
    public BaseCompiler() {
        super();
    }

    /**
     * Establishes an observer on the compiled forms that the compiler outputs.
     *
     * @param observer The compiler output observer.
     */
    public void setCompilerObserver(ICompilerObserver<P, Q> observer) {
        this.observer = observer;
    }

    /**
     * @return
     */
    public IResolver<PC, QC> getResolver() {
        return resolver;
    }

    @Override
    public Logger getConsole() {
        return logger;
    }

    @Override
    public HtPrologParser getParser() {
        return parser;
    }

    @Override
    public void compileDcgRule(DcgRule rule) {

    }

    /**
     * @param fileName
     * @param flags
     * @return
     * @throws Exception
     */
    @Override
    public List<T> compile(String fileName, HtProperty... flags) throws Exception {
        PlLexer ts = PlLexer.getTokenSourceForIoFileName(fileName);
        return compile(ts, flags);
    }

    public List<T> compile(PlLexer tokenSource, HtProperty... flags) throws Exception {
//        getConsole().info("PreCompiling " + tokenSource.getPath() + "... ");
        final List<T> list = new ArrayList<>();
//        parser.setTokenSource(tokenSource);
//        while (tokenSource.isOpen()) {
//            ITerm t = parser.expr(TK_DOT);
//            if (pR.checkBOF())
//            if (t == END_OF_FILE) {
//                parser.popTokenSource();
//            }
//            T c = (T) parser.convert(t);//FIXME
//            compile(c, flags);
//        }

        return list;
    }

    public ICompilerObserver<P, Q> getObserver() {
        return observer;
    }
}
