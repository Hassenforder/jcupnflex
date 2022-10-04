# jcupnflex
Seamless integration of my forked javacup and jflex
## Goal
have one grammar tool which integrate syntax and lexic into one description
+ syntax rules will be redirected to javacup
+ lexical rules will be redirected to jflex
## Ground
Basically the cup grammar is augmented to integrate lexic in several ways
+ first it become possible to define inline regexp. Straight in the grammar rules you can now write the text the scanner have to scan.
+ second you can have new rules with a terminal as lhs and able to express what, when scan a Terminal
+ third you can also have lexical states to build complex scanner
## Inline regexp
regexp are introduced by quoted string in the grammar. Three kind of quotes : simple ('), double ("), back (`).
Inside quotes you can have a quote if you write them twice.
Simple and double quotes will be copied verbatim (with quotes) to the regex to scan for flex.
Back quotes are removed and copied as a regexp to scan for regex.


Exemple :
expr 	::= expr '+' expr
		|	expr "*" expr
		|	expr `div` expr
		|	`[0-9]+`
		;

The rule will be converted into :
expr 	::= expr __REGEXP$1__ expr
		|	expr __REGEXP$2__ expr
		|	expr __REGEXP$3__ expr
		|	__REGEXP$4__
		;

and flex will scan
'+'		{ return symbol(ETerminal.__REGEXP$1__); }
"*"		{ return symbol(ETerminal.__REGEXP$2__); }
div		{ return symbol(ETerminal.__REGEXP$3__); }
[0-9]+	{ return symbol(ETerminal.__REGEXP$4__); }

The quotes around + and * are quite here and play they roles for flex : prevent a wrong interpretation of operators
div was backquoted but for flex they are removed. Relevant if you have pure regeexp to scan like for the number.
No value can be returned by an inlined regexp.


