/**
 * {@link org.ltc.hitalk.gnu.prolog.vm.interpreter.InterpretedCodeCompiler InterpretedCodeCompiler} uses the various classes beginning with I
 * and extending {@link org.ltc.hitalk.gnu.prolog.vm.interpreter.instruction.Instruction Instruction} in the
 * {@link org.ltc.hitalk.gnu.prolog.vm.interpreter.instruction instruction} sub-package to
 * compile clauses to {@link org.ltc.hitalk.gnu.prolog.vm.interpreter.InterpretedByteCode InterpretedByteCode}.
 * 
 * This uses a Warren Abstract Machine model of execution and so reading the paper
 * "An Abstract Prolog Instruction Set" by David Warren (1983, Technical note 309)
 * might help to understand it.
 */
package org.ltc.hitalk.gnu.prolog.vm.interpreter;

