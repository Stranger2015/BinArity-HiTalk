package org.ltc.hitalk.wam.compiler.hitalk;

import org.ltc.hitalk.compiler.IVafInterner;
import org.ltc.hitalk.core.utils.ISymbolTable;
import org.ltc.hitalk.parser.HtPrologParser;
import org.ltc.hitalk.wam.compiler.HtMethod;
import org.ltc.hitalk.wam.compiler.prolog.ICompilerObserver;
import org.ltc.hitalk.wam.compiler.prolog.PrologWAMCompiler;

/**
 * logtalk_library_path(logtalk_third_party_libraries, home('Documents/Logtalk/logtalk_third_party_libraries/')).
 * logtalk_library_path(Project, logtalk_third_party_libraries(ProjectPath)) :-
 * os::directory_files('$HOME/Documents/Logtalk/logtalk_third_party_libraries', Projects, [type(directory), dot_files(false)]),
 * list::member(Project, Projects),
 * atom_concat(Project, '/', ProjectPath).
 *
 * @param <T>
 * @param <P>
 * @param <Q>
 */
public class HiTalkWAMCompiler<T extends HtMethod, P, Q, PC extends HiTalkWAMCompiledPredicate,
        QC extends HiTalkWAMCompiledQuery>
        extends PrologWAMCompiler<T, P, Q, PC, QC> {

//    public HiTalkWAMCompiler () {
//
//    }

    /**
     *
     */
    public enum ImplPolicy {
        NATIVE_LOGTALK,   //using specializer
        PROLOG_CONVERSION,//using specializer
        META_INTERPRETATION,
        PROLOG_MODELLING,//sicstus
        WAM_EXTENSION,
        ;
    }

    /**
     * @param symbolTable
     * @param interner
     * @param parser
     */
    public HiTalkWAMCompiler(ISymbolTable<Integer, String, Object> symbolTable,
                             IVafInterner interner,
                             HtPrologParser parser,
                             ICompilerObserver<P, Q> observer) {
        super(symbolTable, interner, parser, observer);
    }
}
