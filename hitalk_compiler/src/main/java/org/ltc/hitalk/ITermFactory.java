package org.ltc.hitalk;

import com.thesett.aima.logic.fol.Functor;
import com.thesett.aima.logic.fol.Term;
import org.ltc.hitalk.entities.HtEntityIdentifier;
import org.ltc.hitalk.entities.HtEntityKind;
import org.ltc.hitalk.entities.HtProperty;
import org.ltc.hitalk.term.Atom;
import org.ltc.hitalk.term.DottedPair;
import org.ltc.hitalk.term.FloatTerm;
import org.ltc.hitalk.term.IntTerm;

import java.nio.file.Path;

/**
 *
 */
public
interface ITermFactory {

    /**
     * 整数値アトムを生成します。
     */
    Atom newAtom ( int value );

    Atom newAtom ( String value );

    /**
     * 実数値アトムを生成します。
     */
    Atom newAtom ( double value );

    /**
     * ひとつ以上の引数を持つ関数子を作成します。
     */
    Functor newFunctor ( int hilogApply, String value, DottedPair args );

    Functor newFunctor ( int value, DottedPair args );

    /**
     * 変数を作成します。
     */
    Term newVariable ( String value );

    /**
     * @return
     */
//    HiLogCompound createHiLogCompound ( Term term, ListTerm args );

    /**
     * @param s
     * @return
     */
    Functor createAtom ( String s );

    Functor createCompound ( String s, Term[] head, Term tail );

    HtProperty createFlag ( String flagName, String flagValue );

    /**
     * @param name
     * @param args
     * @param kind
     * @return
     */
    HtEntityIdentifier createIdentifier ( HtEntityKind kind, String name, Term... args );

    /**
     * @param name
     * @param args
     * @return
     */
    HtProperty createFlag ( String name, Term... args );

//    /**/Term newFunctor ( Term[] nameHeadTail );

    Functor newFunctor ( int hilogApply, Term name, DottedPair args );

    IntTerm newAtomic ( int i );

    FloatTerm newAtomic ( double f );

    DottedPair newDottedPair ( DottedPair.Kind kind, Term[] terms );

    HtProperty createFlag ( String scratch_directory, Path scratchDir );


//    Term newFunctor ( Term name, Term[] args );

//    HtProperty createProperty ( String name, String value );
//    HtProperty createProperty ( String name, Term... args );

}
/*
    public static final String ANON_VAR = PrologConstants.UNDERSCORE_VAR_NAME;
    public static final Atom IMPLIES = createAtom(":-");
    public static final Atom END_OF_FILE = AtomConstants.END_OF_FILE;
    public static final Atom BEGIN_OF_FILE = AtomConstants.BEGIN_OF_FILE;
    public static final Term[] EMPTY_TERM_ARRAY = new Term[0];
    public static final Term NOP = null;
    public static final Atom ARROW = createAtom("->");
    public static final PredicateIndicator ARROW_2 = createPredicateIndicator(true, Term.ARROW, 2);

    */