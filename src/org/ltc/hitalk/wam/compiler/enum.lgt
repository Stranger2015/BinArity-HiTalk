% if element type is enum `hierarchy` mode
% else `linear` mode
:- object(enum(type, _Elements_ ),
        implements(setp)).

        :- public

                hitalk_type,% :- true by default
                prolog_type,% :- true by default
                user_type   % :- true by default
            .

:- end_object.

% generates by fact that enum has enum elements
:- object(enum(hitalk_type, _),
        implements(setp)).

        :- public
                object,    % :- true by default
                category,  % :- true by default
                protocol,   % :- true by default
                enum,
                module,
                event.


:- end_enum.