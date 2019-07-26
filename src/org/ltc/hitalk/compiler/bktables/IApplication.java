package org.ltc.hitalk.compiler.bktables;

import com.thesett.aima.logic.fol.VariableAndFunctorInterner;
import org.ltc.hitalk.parser.HtPrologParser;

/**
 *
 */
public
interface IApplication {

    /**
     * @return
     */
    IConfig getConfig ();

    /**
     *
     */
    void start () throws Exception;

    /**
     * @return
     */
    int end ();

    /**
     * @return
     */
    boolean isStarted ();

    /**
     * @return
     */
    boolean isStopped ();

    /**
     * @return
     */
    VariableAndFunctorInterner getInterner ();

    /**
     *
     */
    void banner ();

    void setParser ( HtPrologParser parser );
}
