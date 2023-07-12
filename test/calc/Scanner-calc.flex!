
%%

%package calculator
%class Lexer
%public 
%cupJHMH 

%{

    private void fallback () {
		throw new Error("Unrecognized character '"+yytext()+"' -- ignored");
	}    

%}
%{
  private class Region {
  	StringBuilder tmp;
  	int fromLine;
  	int fromColumn;

public Region() {
	super();
	this.tmp = new StringBuilder();
	this.fromLine = yyline;
	this.fromColumn = yycolumn;
}

  }

private java.util.Stack<Region> regions = new java.util.Stack<>();

private void startRegion (int state) {
	regions.push(new Region ());
	yybegin (state);
}

private void appendRegion (String content) {
 if (! regions.empty()) {
  Region region = regions.peek();
  region.tmp.append(content);
 }
}

private Region endRegion (int targetState) {
 Region region = null;
 if (! regions.empty()) {
  region = regions.pop();
 }
    yybegin (targetState);
    return region;
 }

 @SuppressWarnings("unused")
 private Symbol symbolRegion (Region region, ETerminal token) {
    if (region == null) {
  	  AdvancedSymbolFactory.Location position = new AdvancedSymbolFactory.Location (yyline+1, yycolumn+yylength());
  	  return symbolFactory.newSymbol(token, position, position, "");
    } else {
  	  String content = region.tmp.toString();
  	  AdvancedSymbolFactory.Location left = new AdvancedSymbolFactory.Location (region.fromLine+1, region.fromColumn+1);
  	  AdvancedSymbolFactory.Location right = new AdvancedSymbolFactory.Location (yyline+1, yycolumn+yylength());
  	  return symbolFactory.newSymbol(token, left, right, content);
    }
}

%}


%state CODE_STRING$State

%%

<YYINITIAL> {
  "("		{ return symbol(ETerminal.__REGEXP_6__); }
  ")"		{ return symbol(ETerminal.__REGEXP_7__); }
  "*"		{ return symbol(ETerminal.__REGEXP_3__); }
  "+"		{ return symbol(ETerminal.__REGEXP_1__); }
  "-"		{ return symbol(ETerminal.__REGEXP_2__); }
  "/"		{ return symbol(ETerminal.__REGEXP_4__); }
  "="		{ return symbol(ETerminal.__REGEXP_5__); }
  [ \t\f]		{   }
  \r\n|\r|\n		{ return symbol(ETerminal.EOLN); }
  [0-9]+		{ return symbol(ETerminal.NUMBER, Integer.parseInt(yytext())); }
  [A-Za-z_][A-Za-z_0-9]*		{ return symbol(ETerminal.ID, yytext()); }
  "/*"		{ startRegion (CODE_STRING$State); }
}

<CODE_STRING$State> {
  "*/"		{ endRegion (YYINITIAL); }
  [^]		{ appendRegion (yytext()); }
}


[^]			 { fallback(); }

