package org.ltc.hitalk.wam.printer;

import com.thesett.aima.logic.fol.LinkageException;
import org.ltc.hitalk.term.ITermVisitor;
import org.ltc.hitalk.term.ListTerm;

/**
 *
 */
public interface IListTermVisitor extends ITermVisitor {

    IListTermVisitor getIListTermVisitor ();

    /**
     * @param listTerm
     * @throws LinkageException
     */
    default void visit ( ListTerm listTerm ) throws LinkageException {

    }

//    void visit ( ListTerm listTerm1, ListTerm listTerm2 ) throws LinkageException;
}
