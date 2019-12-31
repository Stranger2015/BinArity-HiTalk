package org.ltc.hitalk.compiler;

import com.thesett.common.util.doublemaps.SymbolKey;
import org.ltc.hitalk.core.ICompiler;
import org.ltc.hitalk.core.IResolver;
import org.ltc.hitalk.core.utils.ISymbolTable;
import org.ltc.hitalk.entities.HtProperty;
import org.ltc.hitalk.interpreter.DcgRule;
import org.ltc.hitalk.parser.HtClause;
import org.ltc.hitalk.parser.HtSourceCodeException;
import org.ltc.hitalk.parser.PlPrologParser;
import org.ltc.hitalk.parser.PlTokenSource;
import org.ltc.hitalk.term.ITerm;
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
public class BaseCompiler<T extends HtClause, P, Q> extends AbstractBaseMachine
        implements ICompiler <T, P, Q> {

    protected final Logger logger = LoggerFactory.getLogger(getClass().getSimpleName());

    protected ISymbolTable <Integer, String, Object> scopeTable;
    protected int scope;
    protected Deque <SymbolKey> predicatesInScope = new ArrayDeque <>();
    protected PlPrologParser parser;
    protected IResolver <P, Q> resolver;
    protected ICompilerObserver <P, Q> observer;//todo is that really to be here

    /**
     * @param symbolTable
     * @param interner
     * @param parser
     */
    protected BaseCompiler ( ISymbolTable <Integer, String, Object> symbolTable,
                             IVafInterner interner,
                             PlPrologParser parser,
                             ICompilerObserver <P, Q> observer ) {

        super(symbolTable, interner);

        this.parser = parser;
        this.observer = observer;
    }

    /**
     *
     */
    public BaseCompiler () {
        super();
    }

    /**
     * Establishes an observer on the compiled forms that the compiler outputs.
     *
     * @param observer The compiler output observer.
     */
    public void setCompilerObserver ( ICompilerObserver <P, Q> observer ) {
        this.observer = observer;
    }

    /**
     * @return
     */
    public IResolver <P, Q> getResolver () {
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
    public void compileDcgRule ( DcgRule rule ) throws HtSourceCodeException {

    }

    /**
     * @param fileName
     * @param flags
     * @return
     * @throws Exception
     */
    @Override
    public List <HtClause> compile ( String fileName, HtProperty... flags ) throws Exception {
        PlTokenSource ts = PlTokenSource.getTokenSourceForIoFileName(fileName);
        ts.setPath(fileName);
        return compile(ts, flags);
    }

    @Override
    public List <HtClause> compile ( PlTokenSource tokenSource, HtProperty... flags ) throws Exception {
        getConsole().info("PreCompiling " + tokenSource.getPath() + "... ");
        final List <HtClause> list = new ArrayList <>();
        parser.setTokenSource(tokenSource);
        while (tokenSource.isOpen()) {
            ITerm t = parser.next();
            if (t == PlPrologParser.BEGIN_OF_FILE_ATOM) {
//                getTaskQueue().push(new TermExpansionTask(this, Collections::singletonList,
//                        EnumSet.of(ENCODING))); //read until
                continue;
            }
            if (t == PlPrologParser.END_OF_FILE_ATOM) {
//if (parser.getTokenSource().getStream())
                parser.popTokenSource();
                break;
            }
            T c = (T) parser.convert(t);//FIXME
            compile(c, flags);
        }

        return list;
    }

    public ICompilerObserver <P, Q> getObserver () {
        return observer;
    }
}
