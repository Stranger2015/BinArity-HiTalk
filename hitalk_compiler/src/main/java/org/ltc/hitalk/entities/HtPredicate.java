package org.ltc.hitalk.entities;

import com.thesett.aima.logic.fol.TermTraverser;
import com.thesett.aima.search.Operator;
import com.thesett.common.util.StackQueue;
import org.ltc.hitalk.compiler.IPredicateVisitor;
import org.ltc.hitalk.compiler.IVafInterner;
import org.ltc.hitalk.interpreter.IPredicateTraverser;
import org.ltc.hitalk.parser.HtClause;
import org.ltc.hitalk.term.HtBaseTerm;
import org.ltc.hitalk.term.HtNonVar;
import org.ltc.hitalk.term.ITerm;
import org.ltc.hitalk.term.ITermVisitor;
import org.ltc.hitalk.term.io.HtMethodDef;
import org.ltc.hitalk.wam.compiler.IFunctor;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * properties
 * <p>
 * scope(Scope)             The predicate scope (useful for finding the predicate scope with a single call to predicate_property/2)
 * public,protected,private The predicate scope (useful for testing if a predicate have a specific scope)
 * static,dynamic           All predicates are either static or dynamic (note, however, that a dynamic predicate can only
 * be abolished if it was dynamically declared)
 * logtalk,prolog,foreign   A predicate can be defined in Logtalk source code, Prolog code, or in foreign code(e.g. in C)
 * built_in                 The predicate is a built-in predicate
 * multifile                The predicate is declared multifile (i.e. it can have clauses defined in several entities)
 * meta_predicate(Template) The predicate is declared as a meta-predicate with the specified template
 * coinductive(Template)    The predicate is declared as a coinductive predicate with the specified template
 * declared_in(Entity)      The predicate is declared (using a scope directive) in the specified entity
 * defined_in(Entity)       The predicate l is looked up in the specified entity (note that this property
 * does not necessarily imply that clauses for the predicate exist in Entity; the predicate can simply be false as per
 * the closed-world assumption)
 * redefined_from(Entity)   The predicate is a redefinition of a predicate l inherited from the specified entity
 * non_terminal(NonTerminal//Arity) The predicate resulted from the compilation of the specified grammar rule non-terminal
 * alias_of(Predicate)      The predicate (name) is an alias for the specified predicate
 * alias_declared_in(Entity)The predicate alias is declared in the specified entity
 * synchronized             The predicate is declared as synchronized (i.e.it’s a deterministic predicate synchronized
 * using a mutex when using a backend Prolog compiler supporting a compatible multi-threading implementation)
 * Some properties are only available when the entities are defined in source files and when those source files are
 * compiled with the source_data flag turned on:
 * inline The predicate l is inlined
 * auxiliary The predicate is not user-defined but rather automatically generated by the compiler or
 * the term-expansion mechanism
 * mode(Mode, Solutions) Instantiation, type, and determinism mode for the predicate (which can have multiple modes)
 * number_of_clauses(N) The number of clauses for the predicate existing at co  mpilation time (note that this property
 * is not updated at runtime when asserting and retracting clauses for dynamic predicates)
 * number_of_rules(N) The  number  of  rules  for  the  predicate  existing  at  compilation  time  (note  that  this
 * property is not updated at runtime when asserting and retracting clauses for dynamic predicates)
 * declared_in(Entity, Line)    The predicate is declared (using a scope directive) in the specified entity in
 * a source file at the specified line (if applicable)
 * defined_in(Entity, Line)         The predicate is defined in the specified entity in a source file at the
 * specified line (if applicable)
 * redefined_from(Entity, Line) The predicate is a redefinition of a predicate l inherited from
 * the specified entity, which is defined in a source file at the specified line (if applicable)
 * alias_declared_in(Entity, Line)  The predicate alias is declared in the specified entity in a source file at the
 * specified line (if applicable)
 */
public
class HtPredicate extends HtBaseTerm implements ITerm, IPropertyOwner {
    protected List<HtClause> l;
    /**
     * The clauses that make up this predicate.
     */
//    final protected HtPredicateDefinition l;
    final protected List<PropertyChangeListener> listeners = new ArrayList<>();
    private boolean builtIn;

    public boolean isHiLog () {
        return false;
    }

    /**
     * @return
     */
    public boolean isJavaObject () {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    public ITerm getValue () {
        return this;
    }

    /**
     * @return
     */
    public int size () {
        return l.size();
    }

    /**
     * {@inheritDoc}
     */
    public void free () {
    }

    public void setTermTraverser ( TermTraverser traverser ) {

    }

    /**
     * {@inheritDoc}
     */
    public Iterator <Operator <ITerm>> getChildren ( boolean reverse ) {
        if ((traverser instanceof IPredicateTraverser)) {
            return ((IPredicateTraverser) traverser).traverse(this, reverse);
        } else {
            List <Operator <ITerm>> resultList = null;

            if (!reverse) {
                resultList = new LinkedList <>();//replace linked
            } else {
                resultList = new StackQueue <>();
            }
//
//            if (l != null) {
//                resultList.addAll(Arrays.asList(l));
//            }

            return resultList.iterator();
        }
    }

    /**
     * {@inheritDoc}
     */
    public void accept ( ITermVisitor visitor ) {
        if (visitor instanceof IPredicateVisitor) {
            ((IPredicateVisitor) visitor).visit(this);
        } /*else {
            this.accept(visitor);
        }*/
    }
//
//    public List<ITerm> acceptTransformer ( ITermTransformer transformer ) {
//        return null;
//    }

    /**
     * {@inheritDoc}
     */
    public String toString ( IVafInterner interner, boolean printVarName, boolean printBindings ) {
        String result = IntStream.range(0, size())
                .mapToObj(i -> l.get(i)
                        .toString(interner, printVarName, printBindings) + ((i < (l.size() - 1)) ? "\n" : ""))
                .collect(Collectors.joining());

        return result;
    }

    /**
     * {@inheritDoc}
     */
    public
    String toString () {
        String bodyString = IntStream.range(0, l.size())
                .mapToObj(i -> l.get(i).toString() + ((i < (l.size() - 1)) ? ", " : ""))
                .collect(Collectors.joining("", "[", "]"));

        return String.format("Clause: [ l = %s ]", bodyString);
    }

    //    private final static int PROPS_LENGTH = 29;
    private HtProperty[] props;

    public HtPredicate ( HtClause clause ) {
        this(Collections.singletonList(clause));
    }

    /**
     * Creates a predicate formed from a set of clauses.
     */
    public HtPredicate ( List <HtClause> l, HtProperty... props ) {
        this.l = l;
        this.props = props;
    }

    /**
     * @return
     */
    @Override
    public
    int getPropLength () {
        return props.length;
    }

    @Override
    public void addListener(PropertyChangeListener listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(PropertyChangeListener listener) {
        listeners.remove(listener);
    }

    /**
     * @param propertyName
     * @return
     */
    public HtNonVar getValue(IFunctor propertyName) {
        return null;
    }

    /**
     * @param propertyName
     * @param value
     */
    public void setValue(IFunctor propertyName, HtNonVar value) {

    }

    public HtProperty[] getProps() {
        return new HtProperty[0];
    }

    public HtMethodDef[] getMethods() {
        return new HtMethodDef[0];
    }

    public Map<String, HtMethodDef> getMethodMap() {
        return null;
    }

    public Map<String, HtProperty> getPropMap() {
        return null;
    }

    @Override
    public void fireEvent(IProperty property, HtNonVar value) {
        for (PropertyChangeListener listener : listeners) {
            listener.propertyChange(new PropertyChangeEvent(
                    property,
                    property.getName().toString(),
                    value,
                    property.getValue()));
        }
        //todo
    }

    /**
     * @return
     */
    public List <HtClause> getClauses () {
        return l;
    }

    public boolean isBuiltIn () {
        return builtIn;
    }

    public void setBuiltIn(boolean builtIn) {
        this.builtIn = builtIn;
    }

    public HtClause get(int i) {
        return l.get(i);
    }

    /**
     * This method gets called when a bound property is changed.
     *
     * @param evt A PropertyChangeEvent object describing the event source
     *            and the property that has changed.
     */
    public void propertyChange(PropertyChangeEvent evt) {

    }
}
