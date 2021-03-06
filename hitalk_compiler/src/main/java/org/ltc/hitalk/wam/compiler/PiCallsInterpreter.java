package org.ltc.hitalk.wam.compiler;


import org.ltc.hitalk.compiler.IVafInterner;
import org.ltc.hitalk.core.utils.ISymbolTable;
import org.ltc.hitalk.wam.printer.HtBasePositionalVisitor;
import org.ltc.hitalk.wam.printer.IPositionalTermTraverser;
import org.ltc.hitalk.wam.printer.IPositionalTermVisitor;

public class PiCallsInterpreter extends HtBasePositionalVisitor
        implements IPositionalTermVisitor {

    protected IPositionalTermTraverser positionalTraverser;

    /**
     * Creates a positional visitor.
     *
     * @param symbolTable The compiler symbol table.
     * @param interner    The name interner.
     */
    public PiCallsInterpreter ( ISymbolTable <Integer, String, Object> symbolTable,
                                IVafInterner interner,
                                IPositionalTermTraverser traverser ) {
        super(symbolTable, interner, traverser);
    }

    /**
     * @param positionalTraverser
     */
    public void setPositionalTraverser ( IPositionalTermTraverser positionalTraverser ) {
        this.positionalTraverser = positionalTraverser;
    }
}
