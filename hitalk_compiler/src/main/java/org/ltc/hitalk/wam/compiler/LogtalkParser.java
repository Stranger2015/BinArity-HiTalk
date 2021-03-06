package org.ltc.hitalk.wam.compiler;

import org.ltc.hitalk.ITermFactory;
import org.ltc.hitalk.compiler.IVafInterner;
import org.ltc.hitalk.compiler.bktables.IOperatorTable;
import org.ltc.hitalk.parser.HtPrologParser;

/**
 *
 */
public class LogtalkParser extends HtPrologParser {
    /**
     * @param stream
     * @param interner
     * @param factory
     * @param optable
     */
    public LogtalkParser(HiTalkInputStream stream,
                         IVafInterner interner,
                         ITermFactory factory,
                         IOperatorTable optable) throws Exception {
        super(stream, interner, factory, optable);
    }

}
