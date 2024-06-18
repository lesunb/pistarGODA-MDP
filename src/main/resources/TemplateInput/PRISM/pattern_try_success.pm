[success$PREV_TIME_SLOT$] $PREV_SUCCESS$ $CTX_CONDITION$ s$GID$ = 0 -> (s$GID$'=1);//init to running
	$CTX_EFFECT$
	[fail$PREV_GID$] $PREV_SUCCESS$ s$GID$ = 0 -> (s$GID$'=3);//not used, skip running
