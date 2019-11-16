package org.ltc.hitalk.wam.compiler;

import com.thesett.aima.logic.fol.Term;
import com.thesett.common.util.doublemaps.SymbolTable;
import org.ltc.hitalk.compiler.IVafInterner;
import org.ltc.hitalk.entities.context.ExecutionContext;
import org.ltc.hitalk.entities.context.IMetrics;
import org.ltc.hitalk.parser.HtClause;
import org.ltc.hitalk.wam.transformers.IInliner;
import org.ltc.hitalk.wam.transformers.TransformInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 *
 */
public class Inliner implements IInliner <Term> {

    protected final Logger logger = LoggerFactory.getLogger(getClass().getSimpleName());

    private final SymbolTable <Integer, String, Object> symbolTable;
    private final IVafInterner interner;
    private final List <PiCalls> piCalls;

    /**
     *
     */
    public Inliner ( SymbolTable <Integer, String, Object> symbolTable,
                     IVafInterner interner,
                     List <PiCalls> piCalls ) {
        this.symbolTable = symbolTable;
        this.interner = interner;
        this.piCalls = piCalls;
    }

    /**
     * @param clause
     * @return
     */
    @Override
    public HtClause inline ( HtClause clause ) {
        return clause;
    }

    /**
     *
     */
    @Override
    public void reset () {

    }

    /*
     * @return
     */
    @Override
    public ExecutionContext getContext () {
        return null;
    }

    /**
     * @param context
     */
    @Override
    public void setContext ( ExecutionContext context ) {

    }

    @Override
    public boolean isAcceptable ( IMetrics max ) {
        return false;
    }

    @Override
    public TransformInfo getBestSoFarResult () {
        return null;
    }

    @Override
    public Term transform ( Term t ) {
        return null;
    }

    @Override
    public void cancel () {

    }

    @Override
    public void run () {

    }
}
///
///
// ======================================================================
//    inline predicates
//   ======================================================================
//
//        inline_predicate(<, 2).
//        inline_predicate(>, 2).
//        inline_predicate(=<, 2).
//        inline_predicate(>=, 2).
//        inline_predicate(=:=, 2).
//        inline_predicate(=\=, 2).
//        inline_predicate(is, 2).
//
//        inline_predicate(=, 2).
//        inline_predicate(==, 2).
//        inline_predicate(@=, 2).
//        inline_predicate(\==, 2).
//        inline_predicate(@<, 2).
//        inline_predicate(@=<, 2).
//        inline_predicate(@>, 2).
//        inline_predicate(@>=, 2).
//
//        %%inline_predicate(var, 1).
//        %%inline_predicate(nonvar, 1).
//        inline_predicate('_$cutto', 1).
//        inline_predicate('_$savecp', 1).
//        inline_predicate('_$builtin', 1).
//
//        inline_predicate(fail, 0).
//        inline_predicate(true, 0).
//        inline_predicate(halt, 0).
//        inline_predicate(!, 0).
//
//        inline_predicate(Pred,A) :- inline_jumpcof_cond(Pred,A,_).
//======================================================
//         primitive predicates	("hard-wired" builtins)
//-------------------------------------------------------

//        inline_builtin(psc_name, 2, PSC_NAME).
//        inline_builtin(psc_arity, 2, PSC_ARITY).
//        inline_builtin(psc_type, 2, PSC_TYPE).
//        inline_builtin(psc_prop, 2, PSC_PROP).
//        inline_builtin(psc_mod, 2, PSC_MOD).
//        inline_builtin(psc_set_type, 2, PSC_SET_TYPE).
//        inline_builtin(psc_set_prop, 2, PSC_SET_PROP).
//        inline_builtin(psc_set_spy, 2, PSC_SET_SPY).
//        inline_builtin(psc_ep, 2, PSC_EP).
//        inline_builtin(psc_set_ep, 2, PSC_SET_EP).
//        inline_builtin(psc_data, 2, PSC_DATA).
//
//        inline_builtin(conget, 2, CONGET_TERM).
//        inline_builtin(conset, 2, CONSET_TERM).
//
//        inline_builtin(storage_builtin, 5, STORAGE_BUILTIN).
//
//        inline_builtin(term_new_mod_inline, 3, TERM_NEW_MOD).
//        inline_builtin(term_psc, 2, TERM_PSC).
//        inline_builtin(term_type, 2, TERM_TYPE).
//        inline_builtin(term_compare, 3, TERM_COMPARE).
//        inline_builtin(term_new, 2, TERM_NEW).
//        inline_builtin(term_arg, 3, TERM_ARG).
//        inline_builtin(term_set_arg, 4, TERM_SET_ARG).
//        inline_builtin(stat_flag, 2,  STAT_FLAG).
//        inline_builtin(stat_set_flag, 2, STAT_SET_FLAG).
//        inline_builtin(buff_alloc, 2, BUFF_ALLOC).
//        inline_builtin(buff_word, 3, BUFF_WORD).
//        inline_builtin(buff_set_word, 3, BUFF_SET_WORD).
//        inline_builtin(buff_byte, 3, BUFF_BYTE).
//        inline_builtin(buff_set_byte, 3, BUFF_SET_BYTE).
//        inline_builtin(code_call, 3, CODE_CALL).
//
//        inline_builtin(substring, 4, SUBSTRING).
//        inline_builtin(str_len, 2, STR_LEN).
//        inline_builtin(str_cat, 3, STR_CAT).
//        inline_builtin(str_cmp, 3, STR_CMP).
//        inline_builtin(string_substitute, 4, STRING_SUBSTITUTE).
//
//        inline_builtin(call0, 1, CALL0).
//        inline_builtin(stat_sta, 1, STAT_STA).
//        inline_builtin(stat_cputime, 1, STAT_CPUTIME).
//        inline_builtin(stat_walltime, 1, STAT_WALLTIME).
//        inline_builtin(code_load, 4, CODE_LOAD).
//        inline_builtin(buff_set_var, 4, BUFF_SET_VAR).
//        inline_builtin(buff_dealloc, 3, BUFF_DEALLOC).
//        inline_builtin(buff_cell, 3, BUFF_CELL).
//        inline_builtin(buff_set_cell, 4, BUFF_SET_CELL).
//        inline_builtin(copy_term, 2, COPY_TERM).
//        inline_builtin(str_match, 5, STR_MATCH).
//        inline_builtin(dirname_canonic, 2, DIRNAME_CANONIC).
//        inline_builtin(psc_insert, 4, PSC_INSERT).
//        inline_builtin(psc_import, 3, PSC_IMPORT).
//        inline_builtin(psc_import_as, 2, PSC_IMPORT_AS).
//
//        inline_builtin(psc_insertmod_inline, 3, PSC_INSERTMOD).
//
//        inline_builtin(file_gettoken, 5, FILE_GETTOKEN).
//        inline_builtin(file_puttoken, 3, FILE_PUTTOKEN).
//        inline_builtin(term_hash, 3, TERM_HASH).
//        inline_builtin(unload_seg, 1, UNLOAD_SEG).
//        inline_builtin(load_obj, 4, LOAD_OBJ).
//
//        inline_builtin(getenv, 2, GETENV).
//        /* inline_builtin(sys_syscall, 4, SYS_SYSCALL). */
//        inline_builtin(sys_system, 7, SYS_SYSTEM).
//        inline_builtin(sys_gethost, 2, SYS_GETHOST).
//        inline_builtin(sys_errno, 1, SYS_ERRNO).
//
//        inline_builtin(file_writequoted, 2, FILE_WRITEQUOTED).
//
//        inline_builtin(intern_string, 2, INTERN_STRING).
//        inline_builtin(expand_filename, 2, EXPAND_FILENAME).
//        inline_builtin(tilde_expand_filename, 2, TILDE_EXPAND_FILENAME).
//        inline_builtin(is_absolute_filename, 1, IS_ABSOLUTE_FILENAME).
//        inline_builtin(parse_filename, 4, PARSE_FILENAME).
//        inline_builtin(almost_search_module, 5, ALMOST_SEARCH_MODULE).
//        inline_builtin(existing_file_extension, 2, EXISTING_FILE_EXTENSION).
//
//        inline_builtin(psc_env, 2, PSC_ENV).
//        inline_builtin(psc_spy, 2, PSC_SPY).
//
//        % Should not be inlined -- Kostis !!
//        % inline_builtin(is_incomplete, 2, IS_INCOMPLETE).
//        inline_builtin(get_ptcp, 1, GET_PTCP).
//        inline_builtin(get_producer_call, 3, GET_PRODUCER_CALL).
//        inline_builtin(slg_not, 1, SLG_NOT).
//        inline_builtin(lrd_success, 2, LRD_SUCCESS).
//
//        inline_builtin(dereference_the_bucket, 2, DEREFERENCE_THE_BUCKET).
//        inline_builtin(pair_psc, 2, PAIR_PSC).
//        inline_builtin(pair_next, 2, PAIR_NEXT).
//        inline_builtin(next_bucket, 2, NEXT_BUCKET).
//
//        inline_builtin(is_xwammode, 1, IS_XWAMMODE).
//        inline_builtin(close_open_tables, 1, CLOSE_OPEN_TABLES).
//
//        inline_builtin(file_function, 7, FILE_FUNCTION).
//        inline_builtin(file_function, 3, FILE_FUNCTION).
//        inline_builtin(slash, 1, SLASH_BUILTIN).
//        inline_builtin(formatted_io, 5, FORMATTED_IO).
//
//        inline_builtin(assert_code_to_buff, 1, ASSERT_CODE_TO_BUFF).
//        inline_builtin(assert_buff_to_clref, 6, ASSERT_BUFF_TO_CLREF).
//        inline_builtin(dynamic_code_function,5, DYNAMIC_CODE_FUNCTION).
//
//        inline_builtin(set_tabled_eval, 2, SET_TABLED_EVAL).
//        inline_builtin(table_status, 4, TABLE_STATUS).
//        inline_builtin(get_call, 3, TRIE_GET_CALL).
//        inline_builtin(get_delay_lists, 2, GET_DELAY_LISTS).
//
//        inline_builtin(get_lastnode_cs_retskel, 4, GET_LASTNODE_CS_RETSKEL).
//        % inline_builtin(breg_retskel, 4, BREG_RETSKEL).
//
//        inline_builtin(abolish_table_info, 0, ABOLISH_ALL_TABLES).
//        inline_builtin(abolish_table_predicate, 1, ABOLISH_TABLE_PREDICATE).
//        inline_builtin(trie_delete_return, 3, TRIE_DELETE_RETURN).
//
//        % The following two are not declared as inlined, because they are inlined
//        % in a special way (through WAM instructions) by the XSB compiler anyway.
//        % inline_builtin(var, 1, VAR).
//        % inline_builtin(nonvar, 1, NONVAR).
//
//        inline_builtin(atom, 1, ATOM).
//        inline_builtin(integer, 1, INTEGER).
//        inline_builtin(real, 1, REAL).
//        inline_builtin(number, 1, NUMBER).
//        inline_builtin(atomic, 1, ATOMIC).
//        inline_builtin(compound, 1, COMPOUND).
//        inline_builtin(callable, 1, CALLABLE).
//        inline_builtin(is_list, 1, IS_LIST).
//        inline_builtin(is_attv, 1, IS_ATTV). % similar to is_list
//        inline_builtin(is_most_general_term, 1, IS_MOST_GENERAL_TERM).
//        inline_builtin(is_charlist, 2, IS_CHARLIST).
//        inline_builtin(is_number_atom, 1, IS_NUMBER_ATOM).
//
//        inline_builtin(functor, 3, FUNCTOR).
//        inline_builtin(arg, 3, ARG).
//        inline_builtin(=.., 2, UNIV).
//        inline_builtin(hilog_arg, 3, HiLog_ARG).
//        % inline_builtin(hilog_univ, 2, HiLog_UNIV).
//
//        inline_builtin(atom_codes, 2, ATOM_CODES).
//        inline_builtin(atom_chars, 2, ATOM_CHARS).
//        inline_builtin(number_chars, 2, NUMBER_CHARS).
//        inline_builtin(put, 1, PUT).
//        inline_builtin(tab, 1, TAB).
//        inline_builtin(number_codes, 2, NUMBER_CODES).
//        inline_builtin(number_digits, 2, NUMBER_DIGITS).
//
//        inline_builtin(sort, 2, SORT).
//        inline_builtin(keysort, 2, KEYSORT).
//
//        inline_builtin('$$set_scope_marker', 0, SET_SCOPE_MARKER).
//        inline_builtin('$$unwind_stack', 0, UNWIND_STACK).
//        inline_builtin('$$clean_up_block', 0, CLEAN_UP_BLOCK).
//
//        %inline_builtin(print_ls, 0, PRINT_LS).
//        %inline_builtin(print_tr, 0, PRINT_TR).
//        %inline_builtin(print_heap, 2, PRINT_HEAP).
//        %inline_builtin(print_cp, 0, PRINT_CP).
//        %inline_builtin(print_regs, 0, PRINT_REGS).
//        %inline_builtin(print_all_stacks, 0, PRINT_ALL_STACKS).
//        inline_builtin(expand_heap, 0, EXP_HEAP).
//        inline_builtin(mark_heap, 1, MARK_HEAP).
//        inline_builtin('$$findall_init', 2, FINDALL_INIT).
//        inline_builtin('$$findall_add', 3, FINDALL_ADD).
//        inline_builtin('$$findall_get_solutions', 4, FINDALL_GET_SOLS).
//
//        inline_builtin(force_truth_value, 2, FORCE_TRUTH_VALUE).
//
//        %%inline_jumpcof_cond(_,_,_) :- fail.
//        inline_jumpcof_cond(atom, 1, ATOM_TEST).
//        inline_jumpcof_cond(integer, 1, INTEGER_TEST).
//        inline_jumpcof_cond(real, 1, REAL_TEST).
//        inline_jumpcof_cond(number, 1, NUMBER_TEST).
//        inline_jumpcof_cond(atomic, 1, ATOMIC_TEST).
//        inline_jumpcof_cond(compound, 1, COMPOUND_TEST).
//        inline_jumpcof_cond(callable, 1, CALLABLE_TEST).
//        inline_jumpcof_cond(directly_callable, 1, DIRECTLY_CALLABLE_TEST).
//        inline_jumpcof_cond(is_list, 1, IS_LIST_TEST).
//        inline_jumpcof_cond(is_most_general_term, 1, IS_MOST_GENERAL_TERM_TEST).
//        inline_jumpcof_cond(is_attv, 1, IS_ATTV_TEST).
//        inline_jumpcof_cond(var, 1, VAR_TEST).
//        inline_jumpcof_cond(nonvar, 1, NONVAR_TEST).
//        inline_jumpcof_cond(is_number_atom, 1, IS_NUMBER_ATOM_TEST).
//        inline_jumpcof_cond(ground, 1, GROUND_TEST).
//
//
//        builtin_function(+, 2, 1).
//        builtin_function(-, 2, 2).
//        builtin_function(*, 2, 3).
//        builtin_function(/, 2, 4).
//        builtin_function(/\, 2, 5).
//        builtin_function(\/, 2, 6).
//        builtin_function(//, 2, 7).
//        builtin_function(sin, 1, 9).
//        builtin_function(cos, 1, 10).
//        builtin_function(tan, 1, 11).
//        builtin_function(float, 1, 13).
//        builtin_function(floor, 1, 14).
//        builtin_function(exp, 1, 15).
//        builtin_function(log, 1, 16).
//        builtin_function(log10, 1, 17).
//        builtin_function(sqrt, 1, 18).
//        builtin_function(asin, 1, 19).
//        builtin_function(acos, 1, 20).
//        builtin_function(atan, 1, 21).
//        builtin_function(abs, 1, 22).
//        builtin_function(truncate, 1, 23).
//        builtin_function(round, 1, 24).
//        builtin_function(ceiling, 1, 25).
//        builtin_function(sign, 1, 26).
//        builtin_function(lgamma, 1, 28).
//        builtin_function(erf, 1, 29).
//