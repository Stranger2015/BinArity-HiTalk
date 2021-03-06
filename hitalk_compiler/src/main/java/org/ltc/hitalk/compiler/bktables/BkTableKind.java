package org.ltc.hitalk.compiler.bktables;

import org.ltc.hitalk.compiler.bktables.db.Record;

public
enum BkTableKind {
    BEFORE_EVENTS,//<HtEvent>
    AFTER_EVENTS,

    LOADED_ENTITIES,
    ENTITY_RELATIONS,
    ENTITY_PROPERTIES,
    PREDICATE_PROPERTIES,

    LOADED_FILES,

    RUNTIME_FLAGS,
    STATIC_BINDING_CACHES,
    DYNAMIC_BINDING_LOOKUP_CACHES_1,
    //dynamic binding lookup caches for messages and super calls
//        SEND_TO_OBJ(DYNAMIC_BINDING_LOOKUP_CACHES_1),
//        SEND_TO_OBJ_NE(DYNAMIC_BINDING_LOOKUP_CACHES_1),
//        SEND_TO_SELF(DYNAMIC_BINDING_LOOKUP_CACHES_1),
//        OBJ_SUPERCALL(DYNAMIC_BINDING_LOOKUP_CACHES_1),
//        CTG_SUPERCALL(DYNAMIC_BINDING_LOOKUP_CACHES_1),
    //
//        //Send_to_obj_(Obj, Pred, ExCtx)
//        :- dynamic(Send_to_obj_'/3).
//        //Send_to_obj_ne_(Obj, Pred, ExCtx)
//        :- dynamic(Send_to_obj_ne_'/3).
//        //Send_toSelf_(Obj, Pred, ExCtx)
//        :- dynamic(Send_toSelf_'/3).
//        //objSuperCall_(Super, Pred, ExCtx)
//        :- dynamic(objSuperCall_'/3).
//        //CtgSuperCall_(Ctg, Pred, ExCtx)
//        :- dynamic(CtgSuperCall_'/3).
//
    DYNAMIC_BINDING_LOOKUP_CACHES_2,
    LIBRARY_PATHS,
    EXTENSION_POINTS_FOR_LOGTALK_MAKE,
    GOAL_TERM_EXPANSION_DEFAULT_HOOKS,
    TERM_EXPANSION_DEFAULT_HOOKS(GOAL_TERM_EXPANSION_DEFAULT_HOOKS),
    GOAL_EXPANSION_DEFAULT_HOOKS(GOAL_TERM_EXPANSION_DEFAULT_HOOKS),

    ENGINES,
    COUNTERS,
    DEBUG_HOOK_PREDS,
    INTERNAL_INIT_FLAGS,
    USER_DEFINED_FLAGS,
    ;

    private final BkTableKind parent;
    private Class <? extends Record> bkClass;

    /**
     *
     */
    private
    BkTableKind () {
        this(null, Record.class);
    }

    /**
     * @param parent
     */
    BkTableKind ( BkTableKind parent ) {
        this(parent, Record.class);
    }

    /**
     * @param parent
     * @param clazz
     */
    BkTableKind ( BkTableKind parent, Class <? extends Record> clazz ) {
        this.parent = parent;
        this.bkClass = clazz;
    }

    /**
     * @return
     */
    public
    BkTableKind getParent () {
        return parent;
    }

    /**
     * @return
     */
    public
    Class <? extends Record> getBkClass () {
        return bkClass;
    }
}
 