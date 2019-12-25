package org.ltc.hitalk.wam.compiler.prolog;

import org.ltc.hitalk.parser.HtSourceCodeException;

public interface ICompilerObserver<T, Q> {
    /**
     * Accepts notification of the completion of the compilation of a sentence into a (binary) form.
     *
     * @param sentence The compiled form of the sentence.
     * @throws HtSourceCodeException If there is an error in the compiled code that prevents its further processing.
     */
    void onCompilation ( T sentence ) throws HtSourceCodeException;

    /**
     * Accepts notification of the completion of the compilation of a query into binary form.
     *
     * @param sentence The compiled query.
     * @throws HtSourceCodeException If there is an error in the compiled code that prevents its further processing.
     */
    void onQueryCompilation ( Q sentence ) throws HtSourceCodeException;
}