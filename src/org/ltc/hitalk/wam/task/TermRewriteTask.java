package org.ltc.hitalk.wam.task;

import com.thesett.aima.logic.fol.Term;
import org.ltc.hitalk.compiler.bktables.IComposite;
import org.ltc.hitalk.wam.transformers.ITransformer;

import java.util.ArrayList;
import java.util.List;

/**
 * Availability:built-in
 * prolog_load_context(?Key, ?Value)
 * <p>
 * Obtain context information during compilation.
 * This predicate can be used from directives appearing in a source file
 * to get information about the file being loaded as well as by
 * the term_expansion/2 and goal_expansion/2 hooks.
 * See also source_location/2 and if/1. The following keys are defined:
 * <p>
 * Key	Description
 * <p>
 * directory 	    Directory in which source lives
 * dialect 	    Compatibility mode. See expects_dialect/1.
 * file 	        Similar to source, but returns the file being included
 * when called while an include file is being processed
 * module 	        Module into which file is loaded
 * reload 	        true if the file is being reloaded. Not present on first load
 * script 	        Boolean that indicates whether the file is loaded as a script file (see -s)
 * source 	        File being loaded. If the system is processing an included file, the value is the main file.
 * Returns the original Prolog file when loading a .qlf file.
 * stream 	        Stream identifier (see current_input/1)
 * term_position 	Start position of last term read.
 * See also stream_property/2 (position property and stream_position_data/3.
 * term 	        Term being expanded by expand_term/2.
 * variable_names	A list of `Name = Var' of the last term read. See read_term/2 for details.
 */
public
class TermRewriteTask<T extends Term, TT extends TermRewriteTask <T, TT>> extends StandardPreprocessor <T> implements IComposite <T, TT>, ITransformer <T> {

    protected List <TT> rewriterTasks = new ArrayList <>();

    /**
     * @param target
     * @param transformer
     */
    public
    TermRewriteTask ( T target, ITransformer <T> transformer ) {
        super(target, transformer);
    }

    /**
     * @param t
     */
    @Override
    public
    void add ( TT t ) {
        rewriterTasks.add(t);
    }

    /**
     * @return
     */
    @Override
    public
    List <TT> getComponents () {
        return rewriterTasks;
    }
}
