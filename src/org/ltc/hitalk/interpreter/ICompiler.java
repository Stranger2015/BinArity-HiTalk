package org.ltc.hitalk.interpreter;

import com.thesett.aima.logic.fol.LogicCompiler;
import com.thesett.aima.logic.fol.Resolver;
import com.thesett.aima.logic.fol.Sentence;
import com.thesett.common.parsing.SourceCodeException;
import org.ltc.hitalk.compiler.bktables.HiTalkFlag;
import org.ltc.hitalk.parser.HtClause;
import org.ltc.hitalk.parser.HtPrologParser;
import org.ltc.hitalk.wam.compiler.HtTokenSource;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.thesett.aima.logic.fol.isoprologparser.TokenSource.*;

/**
 * @param <T>
 * @param <T1>
 * @param <T2>
 */
public
interface ICompiler<T extends HtClause, T1, T2> extends LogicCompiler <T, T1, T2> {

    HiTalkFlag[] EMPTY_FLAG_ARRAY = new HiTalkFlag[0];

    /**
     * @param fnl
     * @throws IOException
     * @throws SourceCodeException
     */
    default
    void compileFiles ( List <String> fnl ) throws IOException, SourceCodeException {
        compileFiles(fnl, EMPTY_FLAG_ARRAY);
    }

    default
    void compileFiles ( List <String> fnl, HiTalkFlag... flags ) throws IOException, SourceCodeException {
        for (String fn : fnl) {
            compileFile(fn, flags);
        }
    }

    /**
     * @param fn
     * @throws IOException
     */
    default
    void compileFile ( String fn, HiTalkFlag... flags ) throws IOException, SourceCodeException {
        compile((HtTokenSource) getTokenSourceForFile(new File(fn)), flags);
    }

    /**
     * @param fn
     * @param flags
     * @throws IOException
     * @throws SourceCodeException
     */
    default
    void compileString ( String fn, HiTalkFlag... flags ) throws IOException, SourceCodeException {
        compile((HtTokenSource) getTokenSourceForString(fn), flags);
    }

    /**
     * @param input
     * @param flags
     * @throws IOException
     * @throws SourceCodeException
     */
    default
    void compileInputStream ( InputStream input, HiTalkFlag... flags ) throws IOException, SourceCodeException {
        compile((HtTokenSource) getTokenSourceForInputStream(input), flags);
    }

//    /**
//     * @param fn
//     * @param flags
//     * @throws IOException
//     * @throws SourceCodeException
//     */
//    default
//    void compileZipArchive ( String fn, ZipFile zipFile, HiTalkFlag... flags ) throws IOException, SourceCodeException {
//        ZipEntry zipEntry = zipFile.getEntry(fn);
//        InputStream input = zipFile.getInputStream(zipEntry);
//        compileInputStream(input, flags);
//    }

    /**
     * @param tokenSource
     * @param flags
     */
    default
    void compile ( HtTokenSource tokenSource, HiTalkFlag... flags ) {
        getParser().setTokenSource(tokenSource);
        try {
            while (true) {
                // Parse the next sentence or directive.
                Sentence <HtClause> sentence = getParser().parse();

                getConsole().info(sentence.toString());
                compile(sentence.getT(), flags);
            }
        } catch (Exception e) {
            getConsole().log(Level.SEVERE, e.getMessage(), e);
            System.exit(1);
        }
    }

    /**
     * @return
     */
    Logger getConsole ();

    /**
     * @return
     */
    HtPrologParser getParser ();

    /**
     * @param clause
     * @throws SourceCodeException
     */
    void compile ( HtClause clause, HiTalkFlag... flags ) throws SourceCodeException;

    /**
     * @param rule
     */
    void compileDcgRule ( DcgRule rule ) throws SourceCodeException;

    /**
     * @param query
     */
    void compileQuery ( HtClause query ) throws SourceCodeException;

    /**
     * @param clause
     */
    void compileClause ( HtClause clause );

    /**
     * @param resolver
     */
    void setResolver ( Resolver <T1, T2> resolver );
}
