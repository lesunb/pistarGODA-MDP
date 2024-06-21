$DEC_HEADER$$CONST_PARAM$ double rTask$GID$$DEFAULT_VAL$;

module $MODULE_NAME$
	s$GID$ :[0..4] init 0;	

	$DEC_TYPE$
	[] s$GID$ =  1 -> rTask$GID$ : (s$GID$'=2) + (1 - rTask$GID$) : (s$GID$'=4);//running to final state
	[success$TIME_SLOT$] s$GID$ = 2 -> (s$GID$'=2);//final state success
	[success$TIME_SLOT$] s$GID$ = 3 -> (s$GID$'=3);//final state skipped
	[fail$GID$] s$GID$ = 4 -> (s$GID$'=4);//final state failure
endmodule
$SEQ_RENAMES$
