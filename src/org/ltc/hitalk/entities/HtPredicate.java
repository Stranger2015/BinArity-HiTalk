package org.ltc.hitalk.entities;

import com.thesett.aima.logic.fol.*;
import com.thesett.aima.search.Operator;
import com.thesett.common.util.StackQueue;
import org.ltc.hitalk.compiler.HtPredicateVisitor;
import org.ltc.hitalk.interpreter.IPredicateTraverser;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
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
 * defined_in(Entity)       The predicate definition is looked up in the specified entity (note that this property
 * does not necessarily imply that clauses for the predicate exist in Entity; the predicate can simply be false as per
 * the closed-world assumption)
 * redefined_from(Entity)   The predicate is a redefinition of a predicate definition inherited from the specified entity
 * non_terminal(NonTerminal//Arity) The predicate resulted from the compilation of the specified grammar rule non-terminal
 * alias_of(Predicate)      The predicate (name) is an alias for the specified predicate
 * alias_declared_in(Entity)The predicate alias is declared in the specified entity
 * synchronized             The predicate is declared as synchronized (i.e.it’s a deterministic predicate synchronized
 * using a mutex when using a backend Prolog compiler supporting a compatible multi-threading implementation)
 * Some properties are only available when the entities are defined in source files and when those source files are
 * compiled with the source_data flag turned on:
 * inline The predicate definition is inlined
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
 * redefined_from(Entity, Line) The predicate is a redefinition of a predicate definition inherited from
 * the specified entity, which is defined in a source file at the specified line (if applicable)
 * alias_declared_in(Entity, Line)  The predicate alias is declared in the specified entity in a source file at the
 * specified line (if applicable)
 */
public
class HtPredicate extends BaseTerm implements Term, IPropertyOwner {
    /**
     * The clauses that make up this predicate.
     */
   protected HtPredicateDefinition definition;

    /**
     * Creates a predicate formed from a set of clauses.
     *
     * @param definition The clauses that make up the getDefinition()  of the predicate.
     */
    public
    HtPredicate ( HtPredicateDefinition definition ) {
        this.definition = definition;
    }

    /**
     * {@inheritDoc}
     */
    public
    Term getValue () {
        return this;
    }

    /**
     * Gets all of the clauses that make up the getDefinition()  of the predicate.
     *
     * @return All of the clauses that make up the getDefinition()  of the predicate.
     */
    public
    HtPredicateDefinition getDefinition () {
        return definition;
    }

    /**
     * {@inheritDoc}
     */
    public
    void free () {
    }

    /**
     * {@inheritDoc}
     */
    public
    Iterator <Operator <Term>> getChildren ( boolean reverse ) {
        if ((traverser instanceof IPredicateTraverser)) {
            return ((IPredicateTraverser) traverser).traverse(this, reverse);
        }
        else {
            List <Operator <Term>> resultList = null;

            if (!reverse) {
                resultList = new LinkedList <>();//replace linked
            }
            else {
                resultList = new StackQueue <>();
            }
//
//            if (definition != null) {
//                resultList.addAll(Arrays.asList(definition));
//            }

            return resultList.iterator();
        }
    }

    /**
     * {@inheritDoc}
     */
    public
    void accept ( TermVisitor visitor ) {
        if (visitor instanceof PredicateVisitor) {
            ((HtPredicateVisitor) visitor).visit(this);
        }
        else {
            super.accept(visitor);
        }
    }

    /**
     * {@inheritDoc}
     */
    public
    String toString ( VariableAndFunctorInterner interner, boolean printVarName, boolean printBindings ) {
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < definition.size(); i++) {
            result.append(definition.get(i).toString(interner, printVarName, printBindings)).
                    append((i < (definition.size() - 1)) ? "\n" : "");
        }

        return result.toString();
    }

    /**
     * {@inheritDoc}
     */
    public
    String toString () {
        String bodyString = IntStream.range(0, definition.size())
                .mapToObj(i -> definition.get(i).toString() + ((i < (definition.size() - 1)) ? ", " : ""))
                .collect(Collectors.joining("", "[", "]"));

        return String.format("Clause: [ definition = %s ]", bodyString);
    }

    //    private final static int PROPS_LENGTH = 29;
    private HtProperty[] props;

    /**
     * Creates a predicate formed from a set of clauses.
     */
    public
    HtPredicate ( HtPredicateDefinition definition, HtProperty... props ) {
        super();
        this.definition = definition;
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
}
