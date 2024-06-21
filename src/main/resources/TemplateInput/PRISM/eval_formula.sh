#!/bin/bash
$PARAMS_BASH$

sed  $REPLACE_BASH$ $1 |  gawk '{print "scale=20;"$0}' | bc
exit 0;
