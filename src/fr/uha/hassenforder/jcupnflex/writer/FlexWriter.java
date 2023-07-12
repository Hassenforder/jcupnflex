package fr.uha.hassenforder.jcupnflex.writer;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import fr.uha.hassenforder.jcupnflex.ErrorManager;
import fr.uha.hassenforder.jcupnflex.model.Directive;
import fr.uha.hassenforder.jcupnflex.model.DirectiveSet;
import fr.uha.hassenforder.jcupnflex.model.Grammar;
import fr.uha.hassenforder.jcupnflex.model.Production;
import fr.uha.hassenforder.jcupnflex.model.RegExp;
import fr.uha.hassenforder.jcupnflex.model.LexicalKind;
import fr.uha.hassenforder.jcupnflex.model.LexicalProduction;

public class FlexWriter extends AbstractWriter {

	/*
	 * sort LexicalProduction by two criterion
	 *   name of the region to be able to create/manage region
	 *   complexity of the rule for ordering them 
	 *   
	 */
	private Map<String, Map<Integer, List<LexicalProduction>>> ordered;

	public FlexWriter(Grammar grammar, DirectiveSet directives, File outputFile) {
		super(grammar, directives, outputFile);
	}

	private StringBuilder writeProperty(String header, String value) {
		return writeProperty (header, value, "");
	}

	private StringBuilder writeCode(String content) {
		return writeCode ("", "%{", content, "%}");
	}

	private void emitImportCode() {
		String code = directives.getSingleValue(Directive.SCANNER_IMPORT_CODE);
		appendLine(code);
	}

	private void emitHeader() {
		String packageName = directives.getSingleValue(Directive.PACKAGE_NAME);
		appendLine(writeProperty ("%package", packageName));
		appendLine(writeProperty ("%class", "Lexer"));
		appendLine(writeProperty ("%public", ""));
		appendLine(writeProperty ("%cupJHMH", ""));
	}

	/**
	 * we have to emit the code proposed by the user but also all buffers used by region
	 * 
	 * private StringBuilder $content;
     * private int $line, $column;
     *
     * where $ represents the region name
	 */
	private void emitFlexCodes() {
		String code = directives.getSingleValue(Directive.SCANNER_CODE);
		appendLine(writeCode (code));
		// if we have at least a key not empty or YYINITIAL (it is a region)
		boolean mustEmit = false;
		for (Map.Entry<String, Map<Integer, List<LexicalProduction>>> entry : ordered.entrySet()) {
			if (entry.getKey().isEmpty() || "YYINITIAL".equals(entry.getKey())) continue;
			mustEmit = true;
		}
		if (mustEmit) {
			appendLine(writeCode ("""
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
				    				    """));
		}
	}

	private StringBuilder writeMacro(String name, String regexp) {
		StringBuilder tmp = new StringBuilder();
		tmp.append(name);
		tmp.append(" = ");
		switch (regexp.charAt(0)) {
		case '\'' :
			tmp.append('"');
			tmp.append(regexp.substring(1, regexp.length()-1));
			tmp.append('"');
			break;
		case '\"' :
			tmp.append(regexp);
			break;
		case '`' :
			tmp.append(regexp.substring(1, regexp.length()-1));
			break;
		default :
			break;
		}
		return tmp;
	}

	private void emitMacros() {
		if (! ordered.containsKey("")) return;
		for (List<LexicalProduction> terminals : ordered.get("").values()) {
			for (LexicalProduction terminal : terminals) {
				// reserved type for macro regexp
				if (! "macro".equals(terminal.getLhs().getType())) continue;
				appendLine(writeMacro (terminal.getLhs().getName(), terminal.getRegexp()));
			}
		}
	}

	private void emitStates() {
		for (Map.Entry<String, Map<Integer, List<LexicalProduction>>> entry : ordered.entrySet()) {
			if (entry.getKey().isEmpty() || "YYINITIAL".equals(entry.getKey())) continue;
			StringBuilder content = new StringBuilder ();
			content.append("%state ");
			content.append(entry.getKey());
			content.append("$State");
			appendLine(content);
		}
	}
	
	private StringBuilder writeRegExpMatcher(String regexp) {
		StringBuilder tmp = new StringBuilder();
		switch (regexp.charAt(0)) {
		case '\'' :
			tmp.append('"');
			tmp.append(regexp.substring(1, regexp.length()-1));
			tmp.append('"');
			break;
		case '\"' :
			tmp.append(regexp);
			break;
		case '`' :
			tmp.append(regexp.substring(1, regexp.length()-1));
			break;
		default :
			break;
		}
		return tmp;
	}
	
	private StringBuilder writeRegExpAction(String type, String name, String code) {
		StringBuilder tmp = new StringBuilder();
		tmp.append("{ ");
		if (code != null) {
			tmp.append(code);
		} else if ("void".equals(type)){
			// do nothing
		} else {
			tmp.append("return symbol(ETerminal.");
			tmp.append(name);
			if (type != null) {
				if ("String".equals(type)) {
					tmp.append(", yytext()");
				}
				if ("Integer".equals(type)) {
					tmp.append(", Integer.parseInt(yytext())");
				}
				if ("Long".equals(type)) {
					tmp.append(", Long.parseLong(yytext())");
				}
				if ("Float".equals(type)) {
					tmp.append(", Float.parseFloat(yytext())");
				}
				if ("Double".equals(type)) {
					tmp.append(", Double.parseDouble(yytext())");
				}
			}
			tmp.append(");");
		}
		tmp.append(" }");
		return tmp;
	}

	private StringBuilder writeSimpleRegExp(String type, String name, String regexp, String code) {
		StringBuilder tmp = new StringBuilder();
		tmp.append("  ");
		tmp.append(writeRegExpMatcher(regexp));
		tmp.append("\t\t");
		tmp.append(writeRegExpAction(type, name, code));
		return tmp;
	}

	private StringBuilder writeSimple(LexicalProduction terminal) {
		return writeSimpleRegExp(terminal.getLhs().getType(), terminal.getLhs().getName(), terminal.getRegexp(), terminal.getCode());
	}
	
	/*
	 * we have to build a line like :
	 * regexp 	{ startRegion({State}); } // state!=null
	 * regexp 	{ startRegion({name}$State); }
	 * 
	 */
	private StringBuilder writeRegionEnter(LexicalProduction terminal) {
		StringBuilder tmp = new StringBuilder();
		tmp.append("  ");
		tmp.append(writeRegExpMatcher(terminal.getRegexp()));
		tmp.append("\t\t");
		tmp.append("{ ");
		if (terminal.getCode() != null) {
			tmp.append(terminal.getCode());
		} else {
			tmp.append("startRegion (");
			if (terminal.getEnterState() != null) {
				tmp.append(terminal.getEnterState());
			} else {
				tmp.append(terminal.getLhs().getName());
				tmp.append("$State");
			}
			tmp.append("); ");
		}
		tmp.append("}");
		return tmp;
	}
	
	/*
	 * we have to build a line like :
	 * regexp		{ appendRegion(yytext()); } // regexp != null
	 * [^]			{ appendRegion(yytext()); }
	 * 
	 * In fact if the terminal is of type void we can skip the appendRegion(yytext)); to spare space
	 * 
	 * where $ represent a unique name base on the terminal name with a dollar sign and the remaining text
	 */
	private StringBuilder writeRegionCollect(LexicalProduction terminal) {
		StringBuilder tmp = new StringBuilder();
		tmp.append("  ");
		if (terminal.getRegexp() != null) {
			tmp.append(writeRegExpMatcher(terminal.getRegexp()));
		} else {
			tmp.append("[^]");
		}
		tmp.append("\t\t");
		tmp.append("{ ");
		if (! "void".equals(terminal.getLhs().getType())) {
			tmp.append("appendRegion (");
			tmp.append("yytext()");
			tmp.append("); ");
		}
		tmp.append("}");
		return tmp;
	}
	
	/*
	 * we have to build a line with three parts like :
     *   regexp { regionTemplate symbolTemplate }
	 * 
     *   regionTemplate==	Region r = endRegion({state}); // state != null
     *   Template== 		Region r = endRegion(YYINITIAL); }
     *   
     *   symbolTemplate==	{empty} // if type is void (comment like)
     *   symbolTemplate==	return symbolRegion (r, {terminal} )
	 */
	private StringBuilder writeRegionLeave(LexicalProduction terminal) {
		StringBuilder tmp = new StringBuilder();
		tmp.append("  ");
		tmp.append(writeRegExpMatcher(terminal.getRegexp()));
		tmp.append("\t\t");
		tmp.append("{ ");
		if (terminal.getCode() != null) {
			tmp.append(terminal.getCode());
		} else {
			if (! "void".equals(terminal.getLhs().getType())) {
				tmp.append("Region r = ");
			}
			tmp.append("endRegion (");
			if (terminal.getEnterState() != null) {
				tmp.append(terminal.getEnterState());
				tmp.append("$State");
			} else {
				tmp.append("YYINITIAL");
			}
			tmp.append("); ");
			if (! "void".equals(terminal.getLhs().getType())) {
				tmp.append("return symbolRegion (r, ETerminal.");
				tmp.append(terminal.getLhs().getName());
				tmp.append("); ");
			}
		}
		tmp.append("}");
		return tmp;
	}
	
	private StringBuilder writeTerminal(LexicalProduction terminal) {
		switch (terminal.getSub()) {
		case ENTER_STATE:	return writeRegionEnter (terminal);
		case IN_STATE:		return writeRegionCollect (terminal);
		case LEAVE_STATE:	return writeRegionLeave (terminal);
		case SIMPLE:		return writeSimple (terminal);
		default:			return null;
		}
	}

	/**
	 * 
	 * 
	 */
	private void emitRules() {
		appendLine("<YYINITIAL> {");
		for (RegExp regexp : grammar.getRegexps().values()) {
			appendLine (writeSimpleRegExp(null, regexp.getName(), regexp.getRegexp(), null));
		}
		for (Map.Entry<String, Map<Integer, List<LexicalProduction>>> entry : ordered.entrySet()) {
			// only empty or YYINITIAL are kept
			if (! entry.getKey().isEmpty() && ! "YYINITIAL".equals(entry.getKey())) continue;
			for (List<LexicalProduction> terminals : entry.getValue().values()) {
				for (LexicalProduction terminal : terminals) {
					if ("macro".equals(terminal.getLhs().getType())) continue;
					appendLine(writeTerminal (terminal));
				}
			}
		}
		appendLine("}");
		newLine();
		for (Map.Entry<String, Map<Integer, List<LexicalProduction>>> entry : ordered.entrySet()) {
			// empty or YYINITIAL are skipped
			if (entry.getKey().isEmpty() || "YYINITIAL".equals(entry.getKey())) continue;
			StringBuilder tmp = new StringBuilder();
			tmp.append("<");
			tmp.append(entry.getKey());
			tmp.append("$State");
			tmp.append(">");
			tmp.append(" {");
			appendLine(tmp);
			for (List<LexicalProduction> terminals : entry.getValue().values()) {
				for (LexicalProduction terminal : terminals) {
					appendLine (writeTerminal (terminal));
				}
			}
			appendLine("}");
			newLine();
		}
		newLine();
		appendLine("[^]\t\t\t { fallback(); }");
	}

	private int count(String regexp, char c) {
		if (regexp == null) return 0;
		int count = 0;
		for (int i = 0; i < regexp.length(); i++) {			
			if (regexp.charAt(i) == c) ++count;
		}
		return count;
	}

	private int computeComplexity(LexicalKind sub, String regexp) {
		int starCount = count(regexp, '*');
		int plusCount = count(regexp, '+');
		int macroCount = count(regexp, '{');
		int charCount = regexp == null ? 3 : regexp.length();
		int kindContribution = 0;
		switch (sub) {
		case ENTER_STATE:	kindContribution=30000; break;
		case IN_STATE:		kindContribution=40000; break;
		case LEAVE_STATE:	kindContribution=20000; break;
		case SIMPLE:		kindContribution=10000; break;
		}
		return kindContribution+starCount*100+macroCount*75+plusCount*50+charCount;
	}

	/*
	 * first map is keyed by region name :
	 *    for simple it could be empty or YYINITIAL
	 *    for region it will be the name of the region
	 *    for fallback it will be empty string
	 * second map is keyed by complexity of the production (terminalKind is encoded in)
	 * list is all productions with the same complexity
	 *
	 */
	protected Map<String, Map<Integer, List<LexicalProduction>>> sortAndOrderProductionByRegionKindAndComplexity(Collection<? extends Production> original) {
		Map<String, Map<Integer, List<LexicalProduction>>> ordered = new TreeMap<>();
		for (Production production : original) {
			LexicalProduction terminal = null;
			if (production instanceof LexicalProduction) terminal = (LexicalProduction) production;
			if (terminal == null) continue;
			String region = terminal.getRegion();
			if (region == null) continue;
			Map<Integer, List<LexicalProduction>> complexities = ordered.get(region);
			if (complexities == null) {
				complexities = new TreeMap<>();
				ordered.put (region, complexities);
			}
			int complexity = computeComplexity (terminal.getSub(), terminal.getRegexp());				
			List<LexicalProduction> list = complexities.get(complexity);
			if (list == null) {
				list = new ArrayList<>();
				complexities.put (complexity, list);
			}
			list.add(terminal);
		}
		return ordered;
	}

	public void generate() {
		safeDelete(outputFile);
		
		if (!open())
			return;
		
		if(ErrorManager.getManager().getFatalCount() == 0 && ErrorManager.getManager().getErrorCount() == 0)
		{
			ordered = sortAndOrderProductionByRegionKindAndComplexity (grammar.getProductions());
			emitImportCode ();
			newLine();
			appendLine("%%");
			newLine ();
			emitHeader ();
			newLine ();
			emitFlexCodes ();
			newLine ();
			emitMacros ();
			newLine ();
			emitStates();
			newLine ();
			appendLine("%%");
			newLine ();
			emitRules();
			newLine ();
			
			close();
			ErrorManager.getManager().emit_info("Generate Lexer : " + outputFile.getPath());			
		}
	}

}
