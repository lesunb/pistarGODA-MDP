$DEC_HEADER$$CONST_PARAM$ double rTask$GID$$DEFAULT_VAL$;
const double maxRetries$GID$=$MAX_RETRIES$;

module $MODULE_NAME$
	s$GID$ :[0..4] init 0;
	tries$GID$ : [0..$MAX_TRIES$] init 0;

	$DEC_TYPE$
	[] s$GID$ = 1 & tries$GID$ < maxRetries$GID$ + 1 -> rTask$GID$ : (s$GID$'=2) + (1 - rTask$GID$) : (tries$GID$'=tries$GID$+1);//try
	[] s$GID$ = 1 & tries$GID$ = maxRetries$GID$ + 1 -> (s$GID$'=4);//no more retries
	[success$TIME_SLOT$] s$GID$ = 2 -> (s$GID$'=2);//final state success
	[success$TIME_SLOT$] s$GID$ = 3 -> (s$GID$'=3);//final state skipped
	[fail$GID$] s$GID$ = 4 -> (s$GID$'=4);//final state failure
endmodule
