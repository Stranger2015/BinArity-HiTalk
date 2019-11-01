package org.ltc.hitalk.interpreter;


import com.thesett.common.util.Sink;
import com.thesett.common.util.Source;
import org.ltc.hitalk.parser.jp.segfault.prolog.parser.PlLexer;
import org.ltc.hitalk.parser.jp.segfault.prolog.parser.PlToken;
import org.ltc.hitalk.parser.jp.segfault.prolog.parser.PlTokenSource;

import java.util.ArrayList;
import java.util.List;

/**
 * Used to buffer tokens.
 */
public class TokenBuffer extends PlTokenSource implements Source <PlToken>, Sink <PlToken> {
    private List <PlToken> tokens = new ArrayList <>();

    /**
     * Builds a token source around the specified token manager.
     *
     * @param lexer
     */
    public TokenBuffer ( PlLexer lexer ) {
        super(lexer);
    }

    /**
     * @param o
     * @return
     */
    public boolean offer ( PlToken o ) {
        return tokens.add(o);
    }

    /**
     * @return
     */
    public PlToken poll () {
        return tokens.remove(0);
    }

    /**
     * @return
     */
    public PlToken peek () {
        return tokens.get(0);
    }

    /**
     *
     */
    public
    void clear () {
        tokens.clear();
    }
}
